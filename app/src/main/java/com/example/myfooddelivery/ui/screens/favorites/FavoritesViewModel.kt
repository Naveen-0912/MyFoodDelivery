package com.example.myfooddelivery.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesViewModel(private val databaseProvider: DatabaseProvider,
                         private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper
) : ViewModel() {

    private suspend fun getCustomerAccount() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext loggedInAccountDataStoreHelper.getLoggedInAccount()
    }

    suspend fun getFavoriteHotels() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        val allHotels = databaseProvider.hotelAccountRepository.getAllAccounts()
        return@withContext allHotels.filter { it.hotelId in getCustomerAccount()?.favoriteHotels!! }
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