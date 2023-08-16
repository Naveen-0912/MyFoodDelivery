package com.example.myfooddelivery.ui.screens.myorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.data.db.entities.Ratings
import com.example.myfooddelivery.constants.OrderStatus
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class ViewOrdersViewModel(private val databaseProvider: DatabaseProvider,
                          private val loggedInDataStoreHelper: LoggedInAccountDataStoreHelper): ViewModel() {

    var updatedHotelId = -1
        private set

    suspend fun getCustomerAccount() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        loggedInDataStoreHelper.getLoggedInAccount()
    }

    suspend fun updateFavoriteHotels(hotelId: Int, isLiked: Boolean) = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
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
            favoriteHotels.toList(),
            customerAccount.customerId
        )
        loggedInDataStoreHelper.clearLoggedInAccount()
        loggedInDataStoreHelper.storeLoggedInAccount(updatedAccount)
        databaseProvider.customerAccountRepository.updateAccount(updatedAccount)
    }

    private suspend fun updateHotelRatingsInDatabase(order: Order, rating: Float) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val hotelAccount = getHotelById(order.hotelId)!!
            val totalRatings = hotelAccount.ratings.totalNoOfRatings
            val averageRatings = hotelAccount.ratings.averageRatings
            var updatedAverageRatings = (totalRatings*averageRatings + rating)/(totalRatings + 1)
            updatedAverageRatings = (updatedAverageRatings*100).roundToInt().toFloat() / 100
            val updatedRatings = Ratings(totalRatings+1, updatedAverageRatings)
            val updatedHotelAccount = HotelAccount(
                hotelAccount.name,
                hotelAccount.address,
                hotelAccount.mobileNo,
                hotelAccount.passwordWrapper,
                hotelAccount.isVeg,
                hotelAccount.imageId,
                hotelAccount.categories,
                updatedRatings,
                hotelAccount.hotelId
            )
            return@withContext databaseProvider.hotelAccountRepository.updateAccount(updatedHotelAccount)
    }

    private suspend fun updateOrderRatingsInDatabase(order: Order, rating: Float) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val updatedOrder = Order(
                order.customerId,
                order.hotelId,
                order.itemsWithQuantity,
                order.orderStatus,
                rating,
                order.orderId
            )
            return@withContext databaseProvider.orderRepository.updateOrder(updatedOrder)
        }

    private suspend fun updateItemRatingsInDatabase(itemWithQuantityWrapper: List<ItemWithQuantityWrapper>, rating: Float) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            var result = true
            itemWithQuantityWrapper.forEach {
                val totalRatings = it.item.ratings.totalNoOfRatings
                val averageRatings = it.item.ratings.averageRatings
                var updatedAverageRatings = (totalRatings*averageRatings + rating)/(totalRatings + 1)
                updatedAverageRatings = (updatedAverageRatings*100).roundToInt().toFloat() / 100
                val updatedRatings = Ratings(totalRatings+1, updatedAverageRatings)
                val updatedItem = Item(
                    it.item.itemName,
                    it.item.itemPrice,
                    it.item.isVeg,
                    it.item.hotelId,
                    it.item.imageId,
                    it.item.category,
                    it.item.description,
                    updatedRatings,
                    it.item.itemId
                )
                result = result && databaseProvider.itemRepository.updateItem(updatedItem)
            }
            return@withContext result
        }

    suspend fun rateOrder(order: Order, rating: Float): Boolean {
        updatedHotelId = order.hotelId
        return updateHotelRatingsInDatabase(order, rating) &&
                updateOrderRatingsInDatabase(order, rating) &&
                updateItemRatingsInDatabase(order.itemsWithQuantity, rating)
    }

    suspend fun getDeliveredOrders() =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.orderRepository.getOrdersByCustomerId(getCustomerAccount()?.customerId!!, OrderStatus.DELIVERED)
        }

    suspend fun getActiveOrders() =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.orderRepository.getOrdersByCustomerIdAsLiveData(getCustomerAccount()?.customerId!!, OrderStatus.ACTIVE)
        }

    suspend fun getCancelledOrders() =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.orderRepository.getOrdersByCustomerId(getCustomerAccount()?.customerId!!, OrderStatus.CANCELLED)
        }

    suspend fun getHotelById(hotelId: Int) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.hotelAccountRepository.getHotelById(hotelId)
        }
}