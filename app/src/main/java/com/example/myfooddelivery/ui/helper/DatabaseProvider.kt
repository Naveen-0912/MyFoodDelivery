package com.example.myfooddelivery.ui.helper

import android.content.Context
import com.example.myfooddelivery.data.db.FoodDeliverySystemDatabase
import com.example.myfooddelivery.data.repository.CustomerAccountRepository
import com.example.myfooddelivery.data.repository.HotelAccountRepository
import com.example.myfooddelivery.data.repository.ItemRepository
import com.example.myfooddelivery.data.repository.OrderRepository

class DatabaseProvider private constructor() {

    companion object {

        private lateinit var db : FoodDeliverySystemDatabase

        private var dbProviderInstance: DatabaseProvider? = null

        operator fun invoke(context: Context) = dbProviderInstance ?: this.let {
            db = FoodDeliverySystemDatabase(context)
            DatabaseProvider().also { dbProviderInstance = it }
        }
    }

    val customerAccountRepository = CustomerAccountRepository(db)
    val hotelAccountRepository = HotelAccountRepository(db)
    val itemRepository = ItemRepository(db)
    val orderRepository = OrderRepository(db)
}