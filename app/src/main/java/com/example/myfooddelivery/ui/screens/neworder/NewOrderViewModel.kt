package com.example.myfooddelivery.ui.screens.neworder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.CartDataStoreHelper
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.constants.FoodCategories
import com.example.myfooddelivery.constants.SortType
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewOrderViewModel(private val databaseProvider: DatabaseProvider,
                        private val cartDataStoreHelper: CartDataStoreHelper,
                        private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper): ViewModel() {

    var hotelId = -1
    var totalPrice = MutableLiveData(0)
    var itemsWithQuantity = mutableMapOf<Item, Int>()
    var totalItemsSelected = 0

    var searchText = ""
    var isVegFilterApplied = false
    var sortType = SortType.NONE
    var foodCategories = listOf<FoodCategories>()
    var selectedCategory = listOf<FoodCategories>()

    suspend fun checkIsCartPresent() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {

        val itemsWithQuantityWrapperList = cartDataStoreHelper.getCartItems()
        if (itemsWithQuantityWrapperList != null) {
            withContext(Dispatchers.Main) {
                totalItemsSelected = itemsWithQuantityWrapperList.sumOf { it.quantity }
                totalPrice.value =
                    itemsWithQuantityWrapperList.sumOf { it.item.itemPrice * it.quantity }
            }
            if (itemsWithQuantityWrapperList.isNotEmpty()) {  //to avoid crash after removing all items from summary page
                if (itemsWithQuantityWrapperList.first().item.hotelId == hotelId) {
                    itemsWithQuantity = cartDataStoreHelper.getCartItems()?.associate { it.item to it.quantity }!!.toMutableMap()
                }
            }
        }
        else {
            itemsWithQuantity = mutableMapOf()
            totalItemsSelected = 0
            withContext(Dispatchers.Main) {
                totalPrice.value = 0
            }
        }
    }

    private suspend fun storeCartItems() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        cartDataStoreHelper.clearCartItems()
        cartDataStoreHelper.storeCartItems(
            itemsWithQuantity.map {
                ItemWithQuantityWrapper(it.key, it.value)
            }
        )
    }

    private suspend fun getItemsSortedByRatings() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext if (isVegFilterApplied)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByRatingsByHotelId(hotelId, searchText, selectedCategory)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByRatingsByHotelId(hotelId, searchText, selectedCategory)
    }

    private suspend fun getItemsSortedByPriceHighToLow() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext if (isVegFilterApplied)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId, searchText, selectedCategory)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId, searchText, selectedCategory)
    }

    private suspend fun getItemsSortedByPriceLowToHigh() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext if (isVegFilterApplied)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId, searchText, selectedCategory)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId, searchText, selectedCategory)
    }

    private suspend fun getItemsWithNoSorting() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext if (isVegFilterApplied)
            databaseProvider.itemRepository.getVegItemsFromSubStringByHotelId(hotelId, searchText, selectedCategory)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringByHotelId(hotelId, searchText, selectedCategory)
    }

    suspend fun getFilteredItems(): List<Item> {
        return when(sortType) {
            SortType.RATINGS -> getItemsSortedByRatings()
            SortType.PRICE_HIGH_TO_LOW -> getItemsSortedByPriceHighToLow()
            SortType.PRICE_LOW_TO_HIGH -> getItemsSortedByPriceLowToHigh()
            else -> getItemsWithNoSorting()
        }
    }

    fun addItemToOrder(item: Item) {
        if (itemsWithQuantity.isEmpty()) {
            totalItemsSelected = 0
            totalPrice.value = 0
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                cartDataStoreHelper.clearCartItems()
            }
        }
        itemsWithQuantity[item] = itemsWithQuantity.getOrDefault(item, 0) + 1
        totalItemsSelected++
        totalPrice.value = totalPrice.value!! + item.itemPrice
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            storeCartItems()
        }
    }

    fun removeItemFromOrder(item: Item) {
        if (itemsWithQuantity.containsKey(item)) {
            if (itemsWithQuantity[item] == 1)
                itemsWithQuantity.remove(item)
            else
                itemsWithQuantity[item] = itemsWithQuantity[item]!! - 1
            totalItemsSelected--
            totalPrice.value = totalPrice.value!! - item.itemPrice
            viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
                storeCartItems()
                if (itemsWithQuantity.isEmpty())
                    cartDataStoreHelper.clearCartItems()
            }
        }
    }

    suspend fun getHotel() =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.hotelAccountRepository.getHotelById(hotelId).let {
                foodCategories = it?.categories!!
                if (selectedCategory.size != 1)
                    selectedCategory = it.categories
                it
            }
        }

    suspend fun getCustomerAccount() = withContext(Dispatchers.IO) {
        return@withContext loggedInAccountDataStoreHelper.getLoggedInAccount()
    }

    suspend fun updateFavoriteHotels(hotelId: Int, isLiked: Boolean) = withContext(Dispatchers.IO) {
        val customerAccount = getCustomerAccount()!!
        val favoriteHotels = customerAccount.favoriteHotels.toMutableList()
        if (isLiked)
            favoriteHotels.add(hotelId)
        else
            favoriteHotels.remove(hotelId)
        val updatedAccount = CustomerAccount(
            customerAccount.name,
            customerAccount.address,
            customerAccount.mobileNo,
            customerAccount.passwordWrapper,
            favoriteHotels,
            customerAccount.customerId
        )
        loggedInAccountDataStoreHelper.clearLoggedInAccount()
        loggedInAccountDataStoreHelper.storeLoggedInAccount(updatedAccount)
        databaseProvider.customerAccountRepository.updateAccount(updatedAccount)
    }
}
