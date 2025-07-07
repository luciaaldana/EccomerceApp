package com.luciaaldana.eccomerceapp.data.cart

import com.luciaaldana.eccomerceapp.core.model.Order
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository
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