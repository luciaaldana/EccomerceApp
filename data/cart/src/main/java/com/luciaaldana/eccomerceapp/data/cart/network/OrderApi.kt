package com.luciaaldana.eccomerceapp.data.cart.network

import com.luciaaldana.eccomerceapp.data.cart.dto.OrderDto
import com.luciaaldana.eccomerceapp.data.cart.dto.OrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApi {
    @POST("orders")
    suspend fun createOrder(@Body order: OrderDto): Response<OrderResponse>
    
    @GET("orders")
    suspend fun getOrders(): Response<List<OrderResponse>>
}