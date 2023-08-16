package com.example.myfooddelivery.data.db.entities

data class Address(
    val no: Int,
    val street: String,
    val area: String,
    val state: String,
    val pinCode: Int
)