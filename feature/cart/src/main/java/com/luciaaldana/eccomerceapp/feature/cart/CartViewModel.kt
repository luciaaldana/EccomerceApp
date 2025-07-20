package com.luciaaldana.eccomerceapp.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.model.Order
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val orderHistoryRepository: OrderHistoryRepository
): ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading.asStateFlow()

    val totalAmount: StateFlow<Double> = cartItems.map { list ->
        list.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 00.00)

    init {
        loadCart()
        simulateScreenLoading()
    }
    
    private fun simulateScreenLoading() {
        viewModelScope.launch {
            delay(600) // Simula tiempo de carga de screen
            _isScreenLoading.value = false
        }
    }

    private fun loadCart() {
        _cartItems.value = cartItemRepository.getCartItems()
    }

    fun add(product: Product) {
        cartItemRepository.addProduct(product)
        loadCart()
    }

    fun remove(product: Product) {
        cartItemRepository.removeProduct(product)
        loadCart()
    }

    fun updateQuantity(product: Product, quantity: Int) {
        cartItemRepository.updateQuantity(product, quantity)
        loadCart()
    }

    fun clearCart() {
        cartItemRepository.clearCart()
        loadCart()
    }

    fun confirmOrder() {
        val cartItems = cartItemRepository.getCartItems()
        if (cartItems.isNotEmpty()) {
            viewModelScope.launch {
                val order = orderHistoryRepository.addOrder(cartItems)
                if (order != null) {
                    cartItemRepository.clearCart()
                    loadCart()
                }
            }
        }
    }
}