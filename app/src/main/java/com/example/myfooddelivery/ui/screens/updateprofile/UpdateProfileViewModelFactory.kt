package com.example.myfooddelivery.ui.screens.updateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class UpdateProfileViewModelFactory(private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper,
                                    private val databaseProvider: DatabaseProvider
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdateProfileViewModel(loggedInAccountDataStoreHelper, databaseProvider) as T
    }
}