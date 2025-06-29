package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.Order

interface OrderHistoryRepository {
    fun getOrders(): List<Order>
    fun addOrder(order: Order)
    fun clearHistory()
}