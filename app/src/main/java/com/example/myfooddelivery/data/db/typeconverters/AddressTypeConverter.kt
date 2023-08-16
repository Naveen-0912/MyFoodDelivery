package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.data.db.entities.Address
import com.google.gson.Gson

class AddressTypeConverter {
    @TypeConverter
    fun addressToString(addressObj: Address): String {
        return Gson().toJson(addressObj)
    }

    @TypeConverter
    fun stringToAddress(addressStr: String): Address {
        return Gson().fromJson(addressStr, Address::class.java)
    }
}