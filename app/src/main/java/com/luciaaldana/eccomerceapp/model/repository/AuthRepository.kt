package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.model.data.MockUser

interface AuthRepository {
    fun login(email: String, password: String): Boolean
    fun register(user: MockUser): Boolean
    fun logout()
}