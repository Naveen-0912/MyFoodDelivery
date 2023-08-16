package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.constants.FoodCategories
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodCategoryListTypeConverter {

    @TypeConverter
    fun categoryListToString(categoryListObj: List<FoodCategories>): String {
        return Gson().toJson(categoryListObj)
    }

    @TypeConverter
    fun stringToCategoryList(categoryListStr: String): List<FoodCategories> {
        //TypeToken<T>() is protected. So create object and inherit from it to get the type
        val type = object: TypeToken<List<FoodCategories>>() {}.type
        return Gson().fromJson(categoryListStr, type)
    }
}
