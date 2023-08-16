package com.example.myfooddelivery.ui.screens.orderdispatched

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.constants.OrderStatus
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class OrderDispatchedViewModel(private val databaseProvider: DatabaseProvider,
                               private val loggedInDataStoreHelper: LoggedInAccountDataStoreHelper
) :  ViewModel() {
    var orderedItemsWithQuantityWrapper: List<ItemWithQuantityWrapper>? = null
    var hotelName = ""
    var hotelId = -1
    var totalPrice = -1
    var orderId = -1

    private val customerId = viewModelScope.async(viewModelScope.coroutineContext + Dispatchers.IO) {
        loggedInDataStoreHelper.getLoggedInAccount()?.customerId!!
    }

    val customerAccount = viewModelScope.async(viewModelScope.coroutineContext + Dispatchers.IO) {
        loggedInDataStoreHelper.getLoggedInAccount()!!
    }

    private suspend fun updateOrderInDatabase(order: Order) = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext databaseProvider.orderRepository.updateOrder(order)
    }


    suspend fun initializeOrderWithOrderId() {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val order = databaseProvider.orderRepository.getOrderByOrderId(orderId)!!
            hotelName = databaseProvider.hotelAccountRepository.getHotelById(order.hotelId)!!.name
            hotelId = order.hotelId
            orderedItemsWithQuantityWrapper = order.itemsWithQuantity
            withContext(Dispatchers.Main) {
                totalPrice = orderedItemsWithQuantityWrapper!!.sumOf {
                    it.item.itemPrice * it.quantity
                }
            }
        }
    }

    suspend fun cancelOrder(): Boolean {
        val result = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val order = databaseProvider.orderRepository.getOrderByOrderId(orderId)!!
            val customerId = customerId.await()
            if(customerId != -1) {
                val updatedOrder = Order(
                    order.customerId,
                    order.hotelId,
                    order.itemsWithQuantity,
                    OrderStatus.CANCELLED,
                    order.rating,
                    orderId
                )
                return@withContext updateOrderInDatabase(updatedOrder)
            }
            else
                return@withContext false
        }
        return result
    }
}