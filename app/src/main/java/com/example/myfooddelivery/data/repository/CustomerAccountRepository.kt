package com.example.myfooddelivery.data.repository

import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.data.db.FoodDeliverySystemDatabase

class CustomerAccountRepository(val db: FoodDeliverySystemDatabase){

    fun insertAccount(account: CustomerAccount): Boolean {
        if(db.getCustomerAccountDao().insertCustomer(account) > 0)
                return true
        return false
    }

    fun updateAccount(account: CustomerAccount): Boolean {
        if(db.getCustomerAccountDao().updateCustomer(account) > 0)
            return true
        return false
    }

    fun getAllAccounts(): List<CustomerAccount> {
        return db.getCustomerAccountDao().getAllCustomers()
    }

    fun getCustomerByMobileNo(mobileNo: String): CustomerAccount? {
        return db.getCustomerAccountDao().getCustomerByMobileNo(mobileNo)
    }
}