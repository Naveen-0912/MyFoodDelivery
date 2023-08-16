package com.example.myfooddelivery.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.myfooddelivery.constants.DataStoreConstants

object DataStoreFactory {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreConstants.DATA_STORE_NAME.value.toString())
}