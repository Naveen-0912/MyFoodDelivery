package com.example.myfooddelivery.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class FavoritesViewModelFactory(private val databaseProvider: DatabaseProvider,
                                private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper
                           ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(databaseProvider, loggedInAccountDataStoreHelper) as T
    }
}