package com.example.myfooddelivery.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfooddelivery.data.db.entities.CustomerAccount

@Dao
interface CustomerAccountDao {
    @Query("SELECT * from customerAccount")
    fun getAllCustomers(): List<CustomerAccount>

    @Query("SELECT * from customerAccount where mobileNo=:mobileNo")
    fun getCustomerByMobileNo(mobileNo: String): CustomerAccount?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCustomer(customerAccount: CustomerAccount): Long

    @Update
    fun updateCustomer(customerAccount: CustomerAccount): Int
}