package com.example.myfooddelivery.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class SearchViewModelFactory(private val databaseProvider: DatabaseProvider) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(databaseProvider) as T
    }
}