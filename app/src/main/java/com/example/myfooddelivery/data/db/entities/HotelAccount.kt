package com.example.myfooddelivery.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfooddelivery.constants.FoodCategories

@Entity(tableName = "hotelAccount")
class HotelAccount(
    name: String,
    address: Address,
    mobileNo: String,
    passwordWrapper: PasswordWrapper,
    val isVeg: Boolean,
    val imageId: Int = -1,
    val categories: List<FoodCategories>,
    @Embedded val ratings: Ratings = Ratings(),
    @PrimaryKey(autoGenerate = true)
    val hotelId: Int = 0
) : Account( name, address, mobileNo, passwordWrapper)