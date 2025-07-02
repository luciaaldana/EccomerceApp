package com.luciaaldana.eccomerceapp.data.network

import com.luciaaldana.eccomerceapp.data.network.dto.ProductDto
import retrofit2.http.GET

interface ProductApi {
    @GET("/foods")
    suspend fun getProducts(): List<ProductDto>
}
