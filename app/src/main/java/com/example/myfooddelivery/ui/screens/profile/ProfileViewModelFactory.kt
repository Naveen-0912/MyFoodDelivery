package com.example.myfooddelivery.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper

class ProfileViewModelFactory(private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(loggedInAccountDataStoreHelper) as T
    }
}