package com.luciaaldana.eccomerceapp.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    @Json(name = "_id")   val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    @Json(name = "hasDrink") val hasDrink: Boolean,
    val createdAt: String,
    val updatedAt: String
)