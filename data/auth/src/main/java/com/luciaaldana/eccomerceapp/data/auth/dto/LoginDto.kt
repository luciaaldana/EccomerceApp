package com.luciaaldana.eccomerceapp.data.auth.dto

data class LoginDto(
    val email: String,
    val encryptedPassword: String
)