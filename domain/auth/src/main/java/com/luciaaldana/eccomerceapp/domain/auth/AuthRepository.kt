package com.luciaaldana.eccomerceapp.domain.auth

import com.luciaaldana.eccomerceapp.core.model.MockUser

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(user: MockUser): Boolean
    fun logout()
    fun updateCurrentUser(updatedUser: MockUser)
    fun getCurrentUser(): MockUser?
}