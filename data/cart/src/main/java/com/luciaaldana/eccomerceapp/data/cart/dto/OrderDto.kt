package com.luciaaldana.eccomerceapp.data.cart.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderDto(
    @Json(name = "orderId") val orderId: String,
    @Json(name = "productIds") val productIds: List<ProductOrderDto>,
    @Json(name = "total") val total: Double,
    @Json(name = "timestamp") val timestamp: Long
)

@JsonClass(generateAdapter = true)
data class ProductOrderDto(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "imageUrl") val imageUrl: String,
    @Json(name = "price") val price: Double,
    @Json(name = "hasDrink") val hasDrink: Boolean = false,
    @Json(name = "quantity") val quantity: Int
)

@JsonClass(generateAdapter = true)
data class OrderResponse(
    @Json(name = "_id") val id: String,
    @Json(name = "orderId") val orderId: String,
    @Json(name = "productIds") val productIds: List<ProductOrderDto>,
    @Json(name = "total") val total: Double,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "updatedAt") val updatedAt: String,
    @Json(name = "__v") val version: Int
)

@JsonClass(generateAdapter = true)
data class OrderErrorResponse(
    @Json(name = "message") val message: String,
    @Json(name = "error") val error: ErrorDetail? = null
)

@JsonClass(generateAdapter = true)
data class ErrorDetail(
    @Json(name = "name") val name: String,
    @Json(name = "message") val message: String
)