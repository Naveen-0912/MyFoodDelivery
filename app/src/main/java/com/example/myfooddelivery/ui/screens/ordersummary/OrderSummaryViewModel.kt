package com.example.myfooddelivery.ui.screens.ordersummary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.CartDataStoreHelper
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.constants.OrderStatus
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderSummaryViewModel(private val databaseProvider: DatabaseProvider,
                            private val loggedInDataStoreHelper: LoggedInAccountDataStoreHelper,
                            private val cartDataStoreHelper: CartDataStoreHelper
) :  ViewModel() {
    var itemWithQuantityWrapperList: MutableList<ItemWithQuantityWrapper> = mutableListOf()
    var hotelName = ""
    var hotelId = -1
    var totalPrice = MutableLiveData(-1)
    var isOrderedItemsEmpty = MutableLiveData(false)
    var orderId = -1

//    private val customerId = viewModelScope.async(viewModelScope.coroutineContext + Dispatchers.IO) {
//        loggedInDataStoreHelper.getLoggedInAccount()?.customerId!!
//    }

    val customerAccount = viewModelScope.async(viewModelScope.coroutineContext + Dispatchers.IO) {
        loggedInDataStoreHelper.getLoggedInAccount()!!
    }

    private suspend fun insertOrderIntoDatabase(order: Order) = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext databaseProvider.orderRepository.insertOrder(order)
    }

    suspend fun initializeCartItems() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        itemWithQuantityWrapperList = cartDataStoreHelper.getCartItems()!!.toMutableList()
        hotelId = itemWithQuantityWrapperList.first().item.hotelId
        hotelName = databaseProvider.hotelAccountRepository.getHotelById(hotelId)!!.name
        withContext(Dispatchers.Main) {
            totalPrice.value = itemWithQuantityWrapperList.sumOf {
                it.item.itemPrice * it.quantity
            }
        }
    }

    private suspend fun storeCartItems() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        cartDataStoreHelper.clearCartItems()
        cartDataStoreHelper.storeCartItems(
            itemWithQuantityWrapperList
        )
    }

    fun addItemToOrder(item: Item) {
        val itemWithQuantityWrapper = itemWithQuantityWrapperList.find { it.item == item }
        itemWithQuantityWrapperList.add(itemWithQuantityWrapperList.indexOf(itemWithQuantityWrapper), ItemWithQuantityWrapper(item, itemWithQuantityWrapper?.quantity!!+1))
        itemWithQuantityWrapperList.remove(itemWithQuantityWrapper)
        totalPrice.value = totalPrice.value!! + item.itemPrice
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            storeCartItems()
        }
    }

    fun removeItemFromOrder(item: Item) {
        val itemWithQuantityWrapper = itemWithQuantityWrapperList.find { it.item == item }
        itemWithQuantityWrapper?.let {
            if (it.quantity > 1)
                itemWithQuantityWrapperList.add(itemWithQuantityWrapperList.indexOf(it), ItemWithQuantityWrapper(item, it.quantity-1))
            itemWithQuantityWrapperList.remove(it)
            totalPrice.value = totalPrice.value!! - item.itemPrice
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                storeCartItems()
                if (itemWithQuantityWrapperList.isEmpty()) {
                    cartDataStoreHelper.clearCartItems()
                }
            }
        }
    }


    suspend fun placeOrder(): Long {
        val result = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val customerId = customerAccount.await().customerId
            if(customerId != -1) {
                val order = Order(
                    customerId,
                    hotelId,
                    itemWithQuantityWrapperList,
                    OrderStatus.ACTIVE
                )
                orderId = order.orderId
                return@withContext insertOrderIntoDatabase(order)
            }
            else
                return@withContext -1
        }
        cartDataStoreHelper.clearCartItems()
        return result
    }
}