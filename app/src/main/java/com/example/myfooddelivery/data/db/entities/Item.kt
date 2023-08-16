package com.example.myfooddelivery.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.myfooddelivery.constants.FoodCategories

@Entity(
    tableName = "items",
    foreignKeys = [ForeignKey(
        entity = HotelAccount::class,
        parentColumns = ["hotelId"],
        childColumns = ["hotelId"]
    )]
)
data class Item(
    val itemName: String,
    val itemPrice: Int,
    val isVeg: Boolean,
    val hotelId: Int,
    val imageId: Int = -1,
    val category: FoodCategories,
    val description: String,
    val ratings: Ratings = Ratings(),
    @PrimaryKey(autoGenerate = true)
    val itemId: Int = 0
)
