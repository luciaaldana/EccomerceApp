package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderHistoryRepositoryImpl @Inject constructor() : OrderHistoryRepository {

    private val orders = mutableListOf<Order>()

    override fun getOrders(): List<Order> = orders.toList()

    override fun addOrder(order: Order) {
        orders.add(order)
    }

    override fun clearHistory() {
        orders.clear()
    }
}