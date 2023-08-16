package com.example.myfooddelivery.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customerAccount")
class CustomerAccount(
    name: String,
    address: Address,
    mobileNo: String,
    passwordWrapper: PasswordWrapper,
    val favoriteHotels: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val customerId: Int = 0
) : Account( name, address, mobileNo, passwordWrapper)