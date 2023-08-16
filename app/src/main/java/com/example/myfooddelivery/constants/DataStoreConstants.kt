package com.example.myfooddelivery.constants

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


enum class DataStoreConstants(val value: Preferences.Key<String>) {
    DATA_STORE_NAME(stringPreferencesKey("foodDataStore")),
    LOGGED_IN_DATA_STORE_KEY(stringPreferencesKey("mobileNo"))
}