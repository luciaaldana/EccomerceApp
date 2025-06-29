package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.CartItem
import com.luciaaldana.eccomerceapp.model.data.Product

interface CartItemRepository {
    fun getCartItems(): List<CartItem>
    fun addProduct(product: Product)
    fun removeProduct(product: Product)
    fun updateQuantity(product: Product, quantity: Int)
    fun clearCart()
}