package com.luciaaldana.eccomerceapp.data.auth

import com.luciaaldana.eccomerceapp.core.model.User
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import com.luciaaldana.eccomerceapp.data.auth.network.UserApi
import com.luciaaldana.eccomerceapp.data.auth.dto.UserRegistrationDto
import com.luciaaldana.eccomerceapp.data.auth.dto.LoginDto
import com.luciaaldana.eccomerceapp.data.auth.dto.UpdateUserDto
import com.luciaaldana.eccomerceapp.data.auth.utils.PasswordEncoder
import com.luciaaldana.eccomerceapp.data.database.dao.UserDao
import com.luciaaldana.eccomerceapp.data.database.entity.toUser
import com.luciaaldana.eccomerceapp.data.database.entity.toUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao
) : AuthRepository {

    private val registeredUsers = mutableListOf(
        User(
            firstName = "Naruto", 
            lastName = "Uzumaki", 
            email = "test@test.com", 
            password = "12345678", 
            nationality = "Japón"
        )
    )

    private var loggedInUser: User? = null

    override suspend fun login(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val encodedPassword = PasswordEncoder.encodePassword(password)
                
                val loginDto = LoginDto(
                    email = email.trim(),
                    encryptedPassword = encodedPassword
                )
                
                val response = userApi.loginUser(loginDto)
                
                when (response.code()) {
                    200 -> {
                        // Login exitoso
                        val loginResponse = response.body()
                        loginResponse?.user?.let { user ->
                            loggedInUser = user
                            // Guardar usuario en la base de datos local
                            user._id?.let { userDao.insertUser(user.toUserEntity()) }
                            // También agregar a la lista local si no existe
                            if (!registeredUsers.any { it.email.equals(user.email, ignoreCase = true) }) {
                                registeredUsers.add(user)
                            }
                        }
                        true
                    }
                    401 -> {
                        // Contraseña incorrecta
                        false
                    }
                    404 -> {
                        // Usuario no encontrado
                        false
                    }
                    else -> {
                        // Error del servidor u otro error
                        false
                    }
                }
            } catch (e: Exception) {
                // Error de red o excepción
                false
            }
        }
    }

    override suspend fun register(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val encodedPassword = PasswordEncoder.encodePassword(user.password)
                
                val registrationDto = UserRegistrationDto(
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    nationality = user.nationality,
                    encryptedPassword = encodedPassword,
                    userImageUrl = user.userImageUrl
                )
                
                val response = userApi.registerUser(registrationDto)
                
                when (response.code()) {
                    201 -> {
                        // Usuario registrado exitosamente
                        val createdUser = response.body()
                        createdUser?.let { user ->
                            registeredUsers.add(user)
                            // Set as logged in user and save to database
                            loggedInUser = user
                            user._id?.let { userDao.insertUser(user.toUserEntity()) }
                        }
                        true
                    }
                    409 -> {
                        // Usuario ya existe
                        false
                    }
                    else -> {
                        // Error del servidor u otro error
                        false
                    }
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override fun getCurrentUser(): User? = loggedInUser

    override fun logout() {
        loggedInUser = null
    }

    override fun updateCurrentUser(updatedUser: User) {
        loggedInUser = updatedUser
        // También actualizar en la base de datos si tiene ID
        updatedUser._id?.let { 
            // Usar viewModelScope o un scope similar en una implementación real
            // Por ahora, solo actualizamos en memoria
        }
    }
    
    suspend fun getUserFromDatabase(email: String): User? {
        return userDao.getUserByEmail(email)?.toUser()
    }
    
    suspend fun saveUserToDatabase(user: User) {
        user._id?.let { userDao.insertUser(user.toUserEntity()) }
    }
    
    override suspend fun updateUserProfile(user: User): Boolean {
        return try {
            val updateDto = UpdateUserDto(
                firstName = user.firstName,
                lastName = user.lastName,
                nationality = user.nationality,
                userImageUrl = user.userImageUrl
            )
            
            val response = userApi.updateUser(user.email, updateDto)
            
            when (response.code()) {
                200 -> {
                    // Update successful
                    val updatedUser = response.body()
                    updatedUser?.let { user ->
                        loggedInUser = user
                        // Update in database
                        user._id?.let { userDao.insertUser(user.toUserEntity()) }
                        // Update in local list
                        val index = registeredUsers.indexOfFirst { it.email.equals(user.email, ignoreCase = true) }
                        if (index != -1) {
                            registeredUsers[index] = user
                        }
                    }
                    true
                }
                404 -> {
                    // User not found
                    false
                }
                else -> {
                    // Other server errors
                    false
                }
            }
        } catch (e: Exception) {
            // Update local data even if API call fails
            loggedInUser = user
            // Update in local list
            val index = registeredUsers.indexOfFirst { it.email.equals(user.email, ignoreCase = true) }
            if (index != -1) {
                registeredUsers[index] = user
            }
            true // Return true since local update succeeded
        }
    }
}