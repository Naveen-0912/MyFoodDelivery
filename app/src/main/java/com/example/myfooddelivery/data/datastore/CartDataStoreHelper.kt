package com.example.myfooddelivery.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.myfooddelivery.data.datastore.DataStoreFactory.dataStore
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

class CartDataStoreHelper(private val context: Context) {

    suspend fun getCartItems(): List<ItemWithQuantityWrapper>? {
        val cartItems = context.dataStore.data.first()[stringPreferencesKey(LoggedInAccountDataStoreHelper(context).getLoggedInAccount()!!.mobileNo)]
        return cartItems?.let {
            val type = object: TypeToken<List<ItemWithQuantityWrapper>>() {}.type
            Gson().fromJson(it, type)
        }
    }

    suspend fun storeCartItems(cartItems: List<ItemWithQuantityWrapper>) {
        context.dataStore.edit {
            it[stringPreferencesKey(LoggedInAccountDataStoreHelper(context).getLoggedInAccount()!!.mobileNo)] = Gson().toJson(cartItems)
        }
    }

    suspend fun clearCartItems(){
        context.dataStore.edit {
            it.remove(stringPreferencesKey(LoggedInAccountDataStoreHelper(context).getLoggedInAccount()!!.mobileNo))
        }
    }
}