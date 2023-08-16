package com.example.myfooddelivery.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class LoginViewModelFactory(
    private val databaseProvider: DatabaseProvider,
    private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(databaseProvider, loggedInAccountDataStoreHelper) as T
    }
}