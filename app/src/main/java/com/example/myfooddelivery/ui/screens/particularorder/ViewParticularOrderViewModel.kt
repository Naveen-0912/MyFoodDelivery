package com.example.myfooddelivery.ui.screens.particularorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ViewParticularOrderViewModel(
    private val databaseProvider: DatabaseProvider
) : ViewModel() {
        var orderId = -1
        var orderedItemsWithQuantityWrapper: List<ItemWithQuantityWrapper>? = null

        suspend fun initializeOrderWithOrderId(): Boolean {
            return withContext(viewModelScope.coroutineContext + Dispatchers.Default) {
                orderedItemsWithQuantityWrapper = databaseProvider.orderRepository.getOrderByOrderId(orderId)?.itemsWithQuantity!!
                return@withContext orderedItemsWithQuantityWrapper != null
            }
        }
}