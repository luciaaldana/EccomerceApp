package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.Product

interface ProductRepository {
    fun getAllProducts(): List<Product>
}