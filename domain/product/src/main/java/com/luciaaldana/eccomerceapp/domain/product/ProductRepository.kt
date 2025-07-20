package com.luciaaldana.eccomerceapp.domain.product

import com.luciaaldana.eccomerceapp.core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: String): Product?
    suspend fun syncProductsFromApi(): List<Product>
    suspend fun refreshProducts(): List<Product>
    suspend fun hasLocalProducts(): Boolean
}