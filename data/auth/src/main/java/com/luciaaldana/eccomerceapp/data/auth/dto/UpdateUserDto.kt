package com.luciaaldana.eccomerceapp.data.auth.dto

data class UpdateUserDto(
    val firstName: String,
    val lastName: String,
    val nationality: String,
    val userImageUrl: String?
)