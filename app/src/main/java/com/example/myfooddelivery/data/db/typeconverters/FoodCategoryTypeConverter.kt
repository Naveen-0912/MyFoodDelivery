package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.constants.FoodCategories
import com.google.gson.Gson

class FoodCategoryTypeConverter {
    @TypeConverter
    fun categoryToString(categoryObj: FoodCategories): String {
        return Gson().toJson(categoryObj)
    }

    @TypeConverter
    fun stringToCategory(categoryStr: String): FoodCategories {
        return Gson().fromJson(categoryStr, FoodCategories::class.java)
    }
}