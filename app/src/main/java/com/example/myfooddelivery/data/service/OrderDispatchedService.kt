package com.example.myfooddelivery.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.OrderCancelConstants
import com.example.myfooddelivery.constants.OrderStatus
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OrderDispatchedService : Service() {
    private var jobDeliver: Job? = null
    private var jobMain: Job? = null
    private val myBinder = MyBinder()
    var orderWithTimer = mutableMapOf<Int, MutableLiveData<Int>>()
    private var isTimerRunning = false //if not used startTimer() will be called multiple times
    private val databaseProvider: DatabaseProvider by lazy { DatabaseProvider(applicationContext) }
    private val notificationId = 7

    inner class MyBinder: Binder() {
        fun getService() : OrderDispatchedService {
            return this@OrderDispatchedService
        }
    }

    fun addOrder(orderId: Int) {
        orderWithTimer[orderId] = MutableLiveData(0)
    }

    fun removeOrder(orderId: Int) {
        orderWithTimer.remove(orderId)
    }

    private suspend fun updateOrderInDatabase(order: Order) = withContext(Dispatchers.IO) {
        return@withContext databaseProvider.orderRepository.updateOrder(order)
    }

    suspend fun deliverOrder(orderId: Int) {
        withContext(Dispatchers.IO) {
            val order = databaseProvider.orderRepository.getOrderByOrderId(orderId)!!
            val updatedOrder = Order(
                order.customerId,
                order.hotelId,
                order.itemsWithQuantity,
                OrderStatus.DELIVERED,
                order.rating,
                orderId
            )
            updateOrderInDatabase(updatedOrder)
        }
    }

    private fun startNotification() {
        val channel = NotificationChannel(resources?.getString(R.string.notification_channel_id), resources?.getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        val intent = Intent(this, BaseActivity::class.java)
            .apply {
                putExtra(Screens.ORDER_SCREEN.name, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification: Notification = NotificationCompat.Builder(this, resources?.getString(R.string.notification_channel_id)!!)
            .setContentTitle(resources?.getString(R.string.notification_content_title))
            .setContentText(resources?.getString(R.string.notification_content_text))
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true) //cannot be dismissed
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE) //to show notification immediately
            .build()
        startForeground(notificationId, notification)
    }

    private fun cancelJobs() {
        jobMain?.cancel()
        jobMain = null
        jobDeliver?.cancel()
        jobDeliver = null
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            val orderIdToBeDelivered = mutableListOf<Int>()
            jobDeliver = CoroutineScope(Dispatchers.Default).launch {
                while (orderWithTimer.isNotEmpty()) {
                    delay(1000)
                    orderWithTimer.forEach {
                        it.value.postValue(it.value.value?.inc())
                        if (it.value.value == OrderCancelConstants.MAX_SECONDS.value-1)
                            orderIdToBeDelivered.add(it.key)
                    }
                    if (orderIdToBeDelivered.isNotEmpty()) {
                        deliverOrder(orderIdToBeDelivered.first())
                        orderWithTimer.remove(orderIdToBeDelivered.first())
                        orderIdToBeDelivered.removeFirst()
                    }
                    if (orderWithTimer.isEmpty()) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        cancelJobs()
                        stopSelf()
                    }
                }
                isTimerRunning = false
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (jobMain == null) {
            startNotification()
            var attempts = 0
            jobMain = CoroutineScope(Dispatchers.Default).launch {
                while (attempts < 5) {
                    delay(1000)
                    attempts++
                    if (orderWithTimer.isNotEmpty()) {
                        startTimer()
                        attempts = 0
                    }
                }
                stopForeground(STOP_FOREGROUND_REMOVE)
                cancelJobs()
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

}