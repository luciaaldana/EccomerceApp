package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.CartItem
import com.luciaaldana.eccomerceapp.model.data.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartItemRepositoryImpl @Inject constructor(): CartItemRepository {

    private val cartItems = mutableListOf<CartItem>()

    override fun getCartItems(): List<CartItem> = cartItems.map {it.copy()}

    override fun addProduct(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if(existing !== null) {
            existing.quantity += 1
        } else {
            cartItems.add(CartItem(product, quantity = 1))
        }
    }

    override fun removeProduct(product: Product) {
        cartItems.removeAll { it.product.id == product.id }
    }

    override fun updateQuantity(product: Product, quantity: Int) {
        val item = cartItems.find { it.product.id == product.id }
        if(item != null) {
            if (quantity <=0) {
                cartItems.remove(item)
            } else {
                item.quantity = quantity
            }
        }
    }

    override fun clearCart() {
        cartItems.clear()
    }
}