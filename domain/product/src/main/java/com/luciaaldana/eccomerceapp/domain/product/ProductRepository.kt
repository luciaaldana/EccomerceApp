package com.luciaaldana.eccomerceapp.domain.product

import com.luciaaldana.eccomerceapp.core.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}