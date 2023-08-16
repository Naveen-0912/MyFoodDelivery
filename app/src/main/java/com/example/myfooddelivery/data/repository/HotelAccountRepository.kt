package com.example.myfooddelivery.data.repository

import com.example.myfooddelivery.data.db.FoodDeliverySystemDatabase
import com.example.myfooddelivery.data.db.entities.HotelAccount

class HotelAccountRepository(val db: FoodDeliverySystemDatabase) {

    fun insertAccount(account: HotelAccount): Boolean {
        if(db.getHotelAccountDao().insertHotel(account) > 0)
            return true
        return false
    }

    fun updateAccount(account: HotelAccount): Boolean {
        if(db.getHotelAccountDao().updateHotel(account) > 0){
            return true
        }
        return false
    }

    fun getAllAccounts(): List<HotelAccount> {
        return db.getHotelAccountDao().getAllHotels()
    }

    fun getHotelById(hotelId: Int): HotelAccount? {
        return db.getHotelAccountDao().getHotelById(hotelId)
    }

    fun getPopularHotels(): List<HotelAccount> {
        return db.getHotelAccountDao().getPopularHotels()
    }

    fun getVegHotelsFromSubString(subString: String): List<HotelAccount> {
        return db.getHotelAccountDao().getVegHotelsFromSubString(subString)
    }

    fun getVegHotelsFromSubStringSortedByRatings(subString: String): List<HotelAccount> {
        return db.getHotelAccountDao().getVegHotelsFromSubStringSortedByRatings(subString)
    }

    fun getAllHotelsFromSubStringSortedByRatings(subString: String): List<HotelAccount> {
        return db.getHotelAccountDao().getAllHotelsFromSubStringSortedByRatings(subString)
    }

    fun getHotelAccountsFromSubString(subString: String): List<HotelAccount> {
        return db.getHotelAccountDao().getAllHotelAccountsFromSubString(subString)
    }

    fun getHotelByMobileNo(mobileNo: String): HotelAccount? {
        return db.getHotelAccountDao().getHotelByMobileNo(mobileNo)
    }
}