package com.luciaaldana.eccomerceapp.data.auth.dto

import com.luciaaldana.eccomerceapp.core.model.User

data class LoginResponse(
    val message: String,
    val user: User? = null
)