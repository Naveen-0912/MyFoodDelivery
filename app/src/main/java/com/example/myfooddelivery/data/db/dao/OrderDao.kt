package com.example.myfooddelivery.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.constants.OrderStatus

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(order: Order): Long

    @Update
    fun updateOrder(order: Order): Int

    @Query("SELECT * from orders")
    fun getAllOrders(): List<Order>

    @Query("SELECT * from orders where customerId = :customerId and orderStatus = :orderStatus")
    fun getOrdersByCustomerIdAsLiveData(customerId: Int, orderStatus: OrderStatus): LiveData<List<Order>>

    @Query("SELECT * from orders where customerId = :customerId and orderStatus = :orderStatus")
    fun getOrdersByCustomerId(customerId: Int, orderStatus: OrderStatus): List<Order>

    @Query("SELECT * from orders where orderId = :orderId")
    fun getOrderByOrderId(orderId: Int): Order?
}