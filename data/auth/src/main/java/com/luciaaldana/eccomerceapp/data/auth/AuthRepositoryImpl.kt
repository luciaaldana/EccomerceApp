package com.luciaaldana.eccomerceapp.data.auth

import com.luciaaldana.eccomerceapp.core.model.MockUser
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val registeredUsers = mutableListOf(
        MockUser(email = "test@test.com", password = "12345678", fullName = "Naruto Usumaki")
    )

    private var loggedInUser: MockUser? = null

    override suspend fun login(email: String, password: String): Boolean {
        val user = registeredUsers.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        loggedInUser = user
        return user != null
    }

    override suspend fun register(user: MockUser): Boolean {
        if (registeredUsers.any { it.email.equals(user.email.trim(), ignoreCase = true) }) {
            return false
        }
        registeredUsers.add(user)
        return true
    }

    override fun getCurrentUser(): MockUser? = loggedInUser

    override fun logout() {
        loggedInUser = null
    }

    override fun updateCurrentUser(updatedUser: MockUser) {
        loggedInUser = updatedUser
    }
}