package com.luciaaldana.eccomerceapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.model.data.Order
import com.luciaaldana.eccomerceapp.model.repository.CartItemRepository
import com.luciaaldana.eccomerceapp.model.repository.OrderHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val cartRepo: CartItemRepository,
    private val orderRepo: OrderHistoryRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        _orders.value = orderRepo.getOrders()
    }

    fun confirmOrder() {
        val cartItems = cartRepo.getCartItems()
        if (cartItems.isNotEmpty()) {
            val total = cartItems.sumOf { it.product.price * it.quantity }
            val newOrder = Order(items = cartItems, total = total)
            orderRepo.addOrder(newOrder)
            cartRepo.clearCart()
            loadOrders()
        }
    }

    fun clearHistory() {
        orderRepo.clearHistory()
        loadOrders()
    }
}