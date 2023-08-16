package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.data.db.entities.Ratings
import com.google.gson.Gson

class RatingsTypeConverter {
    @TypeConverter
    fun ratingsToString(ratingsObj: Ratings): String {
        return Gson().toJson(ratingsObj)
    }

    @TypeConverter
    fun stringToAddress(ratingsStr: String): Ratings {
        return Gson().fromJson(ratingsStr, Ratings::class.java)
    }
}