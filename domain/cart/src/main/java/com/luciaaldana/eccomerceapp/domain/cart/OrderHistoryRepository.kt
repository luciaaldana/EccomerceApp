package com.luciaaldana.eccomerceapp.domain.cart

import com.luciaaldana.eccomerceapp.core.model.Order

interface OrderHistoryRepository {
    fun getOrders(): List<Order>
    fun addOrder(order: Order)
    fun clearHistory()
}