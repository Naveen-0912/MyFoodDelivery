package com.example.myfooddelivery.ui.screens.ordersummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.data.datastore.CartDataStoreHelper
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class OrderSummaryViewModelFactory(private val databaseProvider: DatabaseProvider,
                                   private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper,
                                   private val cartDataStoreHelper: CartDataStoreHelper) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OrderSummaryViewModel(databaseProvider, loggedInAccountDataStoreHelper, cartDataStoreHelper) as T
    }
}