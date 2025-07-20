package com.luciaaldana.eccomerceapp.data.cart

import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.model.Order
import com.luciaaldana.eccomerceapp.data.cart.mapper.toOrder
import com.luciaaldana.eccomerceapp.data.cart.mapper.toOrderDto
import com.luciaaldana.eccomerceapp.data.cart.network.OrderApi
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderHistoryRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi
) : OrderHistoryRepository {

    private val localOrders = mutableListOf<Order>()

    override suspend fun getOrders(): List<Order> {
        return try {
            val response = withContext(Dispatchers.IO) {
                orderApi.getOrders()
            }
            
            if (response.isSuccessful) {
                val orders = response.body()?.map { it.toOrder() } ?: emptyList()
                localOrders.clear()
                localOrders.addAll(orders)
                orders
            } else {
                localOrders.toList()
            }
        } catch (e: Exception) {
            localOrders.toList()
        }
    }

    override suspend fun addOrder(cartItems: List<CartItem>): Order? {
        return try {
            val orderDto = cartItems.toOrderDto()
            val response = withContext(Dispatchers.IO) {
                orderApi.createOrder(orderDto)
            }
            
            if (response.isSuccessful) {
                val orderResponse = response.body()
                val order = orderResponse?.toOrder()
                order?.let { localOrders.add(it) }
                order
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun clearHistory() {
        localOrders.clear()
    }
}