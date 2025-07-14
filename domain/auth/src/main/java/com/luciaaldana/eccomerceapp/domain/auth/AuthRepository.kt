package com.luciaaldana.eccomerceapp.domain.auth

import com.luciaaldana.eccomerceapp.core.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(user: User): Boolean
    suspend fun updateUserProfile(user: User): Boolean
    fun logout()
    fun updateCurrentUser(updatedUser: User)
    fun getCurrentUser(): User?
}