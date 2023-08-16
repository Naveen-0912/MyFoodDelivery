package com.example.myfooddelivery.ui.screens.particularorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class ViewParticularOrderViewModelFactory(private val databaseProvider: DatabaseProvider)
    : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewParticularOrderViewModel(databaseProvider) as T
    }
}