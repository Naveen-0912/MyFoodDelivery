package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.data.db.entities.Account
import com.google.gson.Gson

class PasswordWrapperTypeConverter {
    @TypeConverter
    fun addressToString(passwordWrapperObj: Account.PasswordWrapper): String {
        return Gson().toJson(passwordWrapperObj)
    }

    @TypeConverter
    fun stringToAddress(passwordWrapperStr: String): Account.PasswordWrapper {
        return Gson().fromJson(passwordWrapperStr, Account.PasswordWrapper::class.java)
    }
}