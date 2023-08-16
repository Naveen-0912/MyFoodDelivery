package com.example.myfooddelivery.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemsWithQuantityWrapperTypeConverter {

    @TypeConverter
    fun itemsWithQuantityToString(itemsWithQuantityObj: List<ItemWithQuantityWrapper>): String {
        return Gson().toJson(itemsWithQuantityObj)
    }

    @TypeConverter
    fun stringToItemsWithQuantity(itemsWithQuantityStr: String): List<ItemWithQuantityWrapper> {
        //TypeToken<T>() is protected. So create object and inherit from it to get the type
        val type = object: TypeToken<List<ItemWithQuantityWrapper>>() {}.type
        return Gson().fromJson(itemsWithQuantityStr, type)
    }
}
