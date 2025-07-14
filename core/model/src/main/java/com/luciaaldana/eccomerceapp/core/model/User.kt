package com.luciaaldana.eccomerceapp.core.model

import com.squareup.moshi.Json

data class User(
    val _id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    @Json(name = "encryptedPassword") val password: String = "",
    val nationality: String,
    val userImageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null
)