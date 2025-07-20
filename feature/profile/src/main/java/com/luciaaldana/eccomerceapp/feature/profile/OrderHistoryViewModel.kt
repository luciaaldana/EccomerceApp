package com.luciaaldana.eccomerceapp.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.model.Order
import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository
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
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _orders.value = orderRepo.getOrders()
            _isLoading.value = false
        }
    }

    fun confirmOrder() {
        val cartItems = cartRepo.getCartItems()
        if (cartItems.isNotEmpty()) {
            viewModelScope.launch {
                val order = orderRepo.addOrder(cartItems)
                if (order != null) {
                    cartRepo.clearCart()
                    loadOrders()
                }
            }
        }
    }

    fun clearHistory() {
        orderRepo.clearHistory()
        loadOrders()
    }
}