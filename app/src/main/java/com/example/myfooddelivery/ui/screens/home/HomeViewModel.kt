package com.example.myfooddelivery.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel(private val databaseProvider: DatabaseProvider,
                    private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper
) : ViewModel() {
    var selectedCategory: String = ""

    suspend fun getCustomerAccount() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext loggedInAccountDataStoreHelper.getLoggedInAccount()
    }

    suspend fun getAllHotels() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext databaseProvider.hotelAccountRepository.getAllAccounts()
    }

    suspend fun getPopularHotels() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext databaseProvider.hotelAccountRepository.getPopularHotels()
    }

    suspend fun getRandomHotels() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        val hotels = databaseProvider.hotelAccountRepository.getAllAccounts()
        hotels.shuffled().take(hotels.size - 2)
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
        loggedInAccountDataStoreHelper.clearLoggedInAccount()
        loggedInAccountDataStoreHelper.storeLoggedInAccount(updatedAccount)
        databaseProvider.customerAccountRepository.updateAccount(updatedAccount)
    }
}