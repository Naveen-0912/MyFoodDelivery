package com.example.myfooddelivery.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.myfooddelivery.R
import com.example.myfooddelivery.constants.DataStoreConstants
import com.example.myfooddelivery.data.datastore.DataStoreFactory.dataStore
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.first


class LoggedInAccountDataStoreHelper(private val context: Context) {

    suspend fun getLoggedInAccount(): CustomerAccount? {
        val storedAccount = context.dataStore.data.first()[DataStoreConstants.LOGGED_IN_DATA_STORE_KEY.value]
        return storedAccount?.let { Gson().fromJson(it, CustomerAccount::class.java) }
    }

    suspend fun storeLoggedInAccount(customerAccount: CustomerAccount) {
        context.dataStore.edit {
            it[DataStoreConstants.LOGGED_IN_DATA_STORE_KEY.value] = Gson().toJson(customerAccount)
        }
    }

    suspend fun clearLoggedInAccount(){
        context.dataStore.edit {
            it.remove(DataStoreConstants.LOGGED_IN_DATA_STORE_KEY.value)
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getLoggedInAccount() != null
    }
}