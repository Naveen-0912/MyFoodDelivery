package com.example.myfooddelivery.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.myfooddelivery.constants.OrderStatus

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = HotelAccount::class,
        parentColumns = ["hotelId"],
        childColumns = ["hotelId"]
    ),
    ForeignKey(
        entity = CustomerAccount::class,
        parentColumns = ["customerId"],
        childColumns = ["customerId"]
    )]
)
class Order(
    val customerId: Int,
    val hotelId: Int,
    val itemsWithQuantity: List<ItemWithQuantityWrapper>,
    val orderStatus: OrderStatus,
    val rating: Float = -1.0f,
    @PrimaryKey(autoGenerate = true)
    val orderId: Int = 0
)

