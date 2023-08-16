package com.example.myfooddelivery.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfooddelivery.data.db.entities.HotelAccount

@Dao
interface HotelAccountDao {
    @Query("SELECT * from hotelAccount")
    fun getAllHotels(): List<HotelAccount>

    @Query("SELECT * from hotelAccount where hotelId=:hotelId")
    fun getHotelById(hotelId: Int): HotelAccount?

    @Query("SELECT * from hotelAccount where name Like '%' || :subString || '%'")
    fun getAllHotelAccountsFromSubString(subString: String): List<HotelAccount>

    @Query("SELECT * from hotelAccount where isVeg=1 and name Like '%' || :subString || '%'")
    fun getVegHotelsFromSubString(subString: String): List<HotelAccount>

    @Query("SELECT * from hotelAccount where isVeg=1 and name Like '%' || :subString || '%' order by averageRatings DESC")
    fun getVegHotelsFromSubStringSortedByRatings(subString: String): List<HotelAccount>

    @Query("SELECT * from hotelAccount where name Like '%' || :subString || '%' order by averageRatings DESC")
    fun getAllHotelsFromSubStringSortedByRatings(subString: String): List<HotelAccount>


    @Query("SELECT * from hotelAccount where mobileNo=:mobileNo")
    fun getHotelByMobileNo(mobileNo: String): HotelAccount?

    @Query("SELECT * from hotelAccount where averageRatings >4.0 order by averageRatings DESC")
    fun getPopularHotels(): List<HotelAccount>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHotel(hotelAccount: HotelAccount): Long

    @Update
    fun updateHotel(hotelAccount: HotelAccount): Int
}