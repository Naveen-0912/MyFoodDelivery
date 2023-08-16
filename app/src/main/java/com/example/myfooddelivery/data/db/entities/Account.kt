package com.example.myfooddelivery.data.db.entities


abstract class Account(
    val name: String,
    val address: Address,
    val mobileNo: String,
    val passwordWrapper: PasswordWrapper
) {
    class PasswordWrapper(private val password: String) {
        fun authenticate(password: String): Boolean {
            return password == this.password
        }
    }

    fun authenticate(password: String): Boolean {
        return passwordWrapper.authenticate(password)
    }
}