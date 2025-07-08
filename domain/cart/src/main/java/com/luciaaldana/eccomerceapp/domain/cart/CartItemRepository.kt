package com.luciaaldana.eccomerceapp.domain.cart

import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.model.Product

interface CartItemRepository {
    fun getCartItems(): List<CartItem>
    fun addProduct(product: Product)
    fun removeProduct(product: Product)
    fun updateQuantity(product: Product, quantity: Int)
    fun clearCart()
}