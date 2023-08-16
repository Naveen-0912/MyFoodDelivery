package com.example.myfooddelivery.data.repository

import androidx.lifecycle.LiveData
import com.example.myfooddelivery.data.db.FoodDeliverySystemDatabase
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.constants.OrderStatus

class OrderRepository(val db: FoodDeliverySystemDatabase) {

    fun insertOrder(order: Order): Long {
        return db.getOrderDao().insertOrder(order)
    }

    fun updateOrder(order: Order): Boolean {
        if(db.getOrderDao().updateOrder(order) > 0)
            return true
        return false
    }

    fun getOrdersByCustomerIdAsLiveData(customerId: Int, orderStatus: OrderStatus): LiveData<List<Order>> {
        return db.getOrderDao().getOrdersByCustomerIdAsLiveData(customerId, orderStatus)
    }

    fun getOrdersByCustomerId(customerId: Int, orderStatus: OrderStatus): List<Order> {
        return db.getOrderDao().getOrdersByCustomerId(customerId, orderStatus)
    }

    fun getOrderByOrderId(orderId: Int): Order? {
        return db.getOrderDao().getOrderByOrderId(orderId)
    }
}