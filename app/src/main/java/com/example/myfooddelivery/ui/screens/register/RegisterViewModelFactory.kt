package com.example.myfooddelivery.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class RegisterViewModelFactory(private val databaseProvider: DatabaseProvider) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(databaseProvider) as T
    }
}