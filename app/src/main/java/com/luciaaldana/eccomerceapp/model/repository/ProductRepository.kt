package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}