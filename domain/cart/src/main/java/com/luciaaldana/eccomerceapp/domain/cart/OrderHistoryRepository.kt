package com.luciaaldana.eccomerceapp.domain.cart

import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.model.Order

interface OrderHistoryRepository {
    suspend fun getOrders(): List<Order>
    suspend fun addOrder(cartItems: List<CartItem>): Order?
    fun clearHistory()
}