package com.luciaaldana.eccomerceapp.data.product.network

import com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("/foods")
    suspend fun getProducts(): List<ProductDto>
}