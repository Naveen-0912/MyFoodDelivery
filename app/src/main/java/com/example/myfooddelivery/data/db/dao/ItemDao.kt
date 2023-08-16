package com.example.myfooddelivery.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.constants.FoodCategories

@Dao
interface ItemDao {

    @Query("SELECT * from items where hotelId=:hotelId")
    fun getItemByHotelId(hotelId: Int): List<Item>

    @Query("SELECT * from items")
    fun getAllItems(): List<Item>

    @Query("SELECT * from items where itemName Like '%' || :subString || '%'")
    fun getAllItemsFromSubString(subString: String): List<Item>

    @Query("SELECT * from items where isVeg=1 and itemName Like '%' || :subString || '%'")
    fun getVegItemsFromSubString(subString: String): List<Item>

    @Query("SELECT * from items where isVeg=1 and itemName Like '%' || :subString || '%' order by ratings DESC")
    fun getVegItemsFromSubStringSortedByRatings(subString: String): List<Item>

    @Query("SELECT * from items where itemName Like '%' || :subString || '%' order by ratings DESC")
    fun getAllItemsFromSubStringSortedByRatings(subString: String): List<Item>

    @Query("SELECT * from items where itemName Like '%' || :subString || '%' order by itemPrice ASC")
    fun getAllItemsFromSubStringSortedByPriceLowToHigh(subString: String): List<Item>

    @Query("SELECT * from items where isVeg=1 and itemName Like '%' || :subString || '%' order by itemPrice ASC")
    fun getVegItemsFromSubStringSortedByPriceLowToHigh(subString: String): List<Item>

    @Query("SELECT * from items where itemName Like '%' || :subString || '%' order by itemPrice DESC")
    fun getAllItemsFromSubStringSortedByPriceHighToLow(subString: String): List<Item>

    @Query("SELECT * from items where isVeg=1 and itemName Like '%' || :subString || '%' order by itemPrice DESC")
    fun getVegItemsFromSubStringSortedByPriceHighToLow(subString: String): List<Item>


    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and itemName Like '%' || :subString || '%'")
    fun getAllItemsFromSubStringByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and isVeg=1 and itemName Like '%' || :subString || '%'")
    fun getVegItemsFromSubStringByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and isVeg=1 and itemName Like '%' || :subString || '%' order by ratings DESC")
    fun getVegItemsFromSubStringSortedByRatingsByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and itemName Like '%' || :subString || '%' order by ratings DESC")
    fun getAllItemsFromSubStringSortedByRatingsByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and itemName Like '%' || :subString || '%' order by itemPrice ASC")
    fun getAllItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and isVeg=1 and itemName Like '%' || :subString || '%' order by itemPrice ASC")
    fun getVegItemsFromSubStringSortedByPriceLowToHighByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and itemName Like '%' || :subString || '%' order by itemPrice DESC")
    fun getAllItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Query("SELECT * from items where category in (:foodCategories) and hotelId=:hotelId and isVeg=1 and itemName Like '%' || :subString || '%' order by itemPrice DESC")
    fun getVegItemsFromSubStringSortedByPriceHighToLowByHotelId(hotelId: Int, subString: String, foodCategories: List<FoodCategories>): List<Item>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(item: Item): Long

    @Update
    fun updateItem(item: Item): Int

}