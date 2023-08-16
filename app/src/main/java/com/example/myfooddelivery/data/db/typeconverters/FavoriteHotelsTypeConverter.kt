package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteHotelsTypeConverter {

    @TypeConverter
    fun favoriteListToString(favoriteListObj: List<Int>): String {
        return Gson().toJson(favoriteListObj)
    }

    @TypeConverter
    fun stringToFavoriteList(favoriteListStr: String): List<Int> {
        //TypeToken<T>() is protected. So create object and inherit from it to get the type
        val type = object: TypeToken<List<Int>>() {}.type
        return Gson().fromJson(favoriteListStr, type)
    }
}
