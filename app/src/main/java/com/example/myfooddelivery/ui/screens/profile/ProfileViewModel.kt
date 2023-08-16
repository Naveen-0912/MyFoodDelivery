package com.example.myfooddelivery.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileViewModel(private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper) : ViewModel() {

    suspend fun getLoggedInAccount() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        return@withContext loggedInAccountDataStoreHelper.getLoggedInAccount()
    }

    suspend fun logOut() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        loggedInAccountDataStoreHelper.clearLoggedInAccount()
    }
}