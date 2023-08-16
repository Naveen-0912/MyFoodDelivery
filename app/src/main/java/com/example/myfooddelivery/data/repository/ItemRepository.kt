package com.example.myfooddelivery.data.repository

import com.example.myfooddelivery.data.db.FoodDeliverySystemDatabase
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.constants.FoodCategories

class ItemRepository(val db: FoodDeliverySystemDatabase) {
    fun insertItem(item: Item): Boolean {
        if(db.getItemDao().insertItem(item) > 0)
            return true
        return false
    }

    fun updateItem(item: Item): Boolean {
        if(db.getItemDao().updateItem(item) > 0)
            return true
        return false
    }

    fun getAllItemsFromSubString(subString: String): List<Item> {
        return db.getItemDao().getAllItemsFromSubString(subString)
    }

    fun getVegItemsFromSubString(subString: String): List<Item> {
        return db.getItemDao().getVegItemsFromSubString(subString)
    }

    fun getVegItemsFromSubStringSortedByRatings(subString: String): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByRatings(subString)
    }

    fun getAllItemsFromSubStringSortedByRatings(subString: String): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByRatings(subString)
    }

    fun getAllItemsFromSubStringSortedByPriceLowToHigh(subString: String): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByPriceLowToHigh(subString)
    }

    fun getVegItemsFromSubStringSortedByPriceLowToHigh(subString: String): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByPriceLowToHigh(subString)
    }

    fun getAllItemsFromSubStringSortedByPriceHighToLow(subString: String): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByPriceHighToLow(subString)
    }

    fun getVegItemsFromSubStringSortedByPriceHighToLow(subString: String): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByPriceHighToLow(subString)
    }

    fun getAllItemsFromSubStringByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringByHotelId(hotelId,subString, foodCategories)
    }

    fun getVegItemsFromSubStringByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringByHotelId(hotelId, subString, foodCategories)
    }

    fun getVegItemsFromSubStringSortedByRatingsByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByRatingsByHotelId(hotelId, subString, foodCategories)
    }

    fun getAllItemsFromSubStringSortedByRatingsByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByRatingsByHotelId(hotelId, subString, foodCategories)
    }

    fun getAllItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId, subString, foodCategories)
    }

    fun getVegItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId, subString, foodCategories)
    }

    fun getAllItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getAllItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId, subString, foodCategories)
    }

    fun getVegItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item> {
        return db.getItemDao().getVegItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId, subString, foodCategories)
    }
}