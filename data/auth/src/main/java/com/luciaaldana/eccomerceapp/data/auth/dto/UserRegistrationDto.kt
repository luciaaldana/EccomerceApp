package com.luciaaldana.eccomerceapp.data.auth.dto

data class UserRegistrationDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val nationality: String,
    val encryptedPassword: String
)