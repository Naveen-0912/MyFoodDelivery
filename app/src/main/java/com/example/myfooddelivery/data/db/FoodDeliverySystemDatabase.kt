package com.example.myfooddelivery.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myfooddelivery.data.db.dao.CustomerAccountDao
import com.example.myfooddelivery.data.db.dao.HotelAccountDao
import com.example.myfooddelivery.data.db.dao.ItemDao
import com.example.myfooddelivery.data.db.dao.OrderDao
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.data.db.typeconverters.AddressTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.FavoriteHotelsTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.FoodCategoryListTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.FoodCategoryTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.PasswordWrapperTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.ItemsWithQuantityWrapperTypeConverter
import com.example.myfooddelivery.data.db.typeconverters.RatingsTypeConverter

@Database(
    entities = [
        CustomerAccount::class,
        HotelAccount::class,
        Item::class,
        Order::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    AddressTypeConverter::class,
    PasswordWrapperTypeConverter::class,
    RatingsTypeConverter::class,
    ItemsWithQuantityWrapperTypeConverter::class,
    FoodCategoryTypeConverter::class,
    FoodCategoryListTypeConverter::class,
    FavoriteHotelsTypeConverter::class
)
abstract class FoodDeliverySystemDatabase : RoomDatabase() {
    abstract fun getCustomerAccountDao(): CustomerAccountDao
    abstract fun getHotelAccountDao(): HotelAccountDao
    abstract fun getItemDao(): ItemDao
    abstract fun getOrderDao(): OrderDao

    companion object {
        private var dbInstance : FoodDeliverySystemDatabase? = null

        operator fun invoke(context: Context) = dbInstance ?: createDatabase(context).also { dbInstance = it }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                FoodDeliverySystemDatabase::class.java,
                "foodDeliveryDatabase.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}