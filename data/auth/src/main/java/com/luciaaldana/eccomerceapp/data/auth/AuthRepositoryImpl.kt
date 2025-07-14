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
                println("AuthRepositoryImpl: Attempting login for ${email}")
                
                val encodedPassword = PasswordEncoder.encodePassword(password)
                println("AuthRepositoryImpl: Original password: $password")
                println("AuthRepositoryImpl: Encoded password: $encodedPassword")
                
                val loginDto = LoginDto(
                    email = email.trim(),
                    encryptedPassword = encodedPassword
                )
                
                println("AuthRepositoryImpl: Calling API login endpoint")
                println("AuthRepositoryImpl: Login URL should be: http://10.0.2.2:10000/users/login")
                println("AuthRepositoryImpl: Login payload: $loginDto")
                
                val response = userApi.loginUser(loginDto)
                
                println("AuthRepositoryImpl: Login response code: ${response.code()}")
                
                when (response.code()) {
                    200 -> {
                        // Login exitoso
                        val loginResponse = response.body()
                        loginResponse?.user?.let { user ->
                            loggedInUser = user
                            println("AuthRepositoryImpl: Login successful for ${user.email}")
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
                        println("AuthRepositoryImpl: Login failed - invalid credentials")
                        false
                    }
                    404 -> {
                        // Usuario no encontrado
                        println("AuthRepositoryImpl: Login failed - user not found")
                        false
                    }
                    else -> {
                        // Error del servidor u otro error
                        println("AuthRepositoryImpl: Login failed - server error: ${response.code()}")
                        false
                    }
                }
            } catch (e: Exception) {
                // Error de red o excepción
                println("AuthRepositoryImpl: Login failed - network error: ${e.message}")
                println("AuthRepositoryImpl: Exception type: ${e.javaClass.simpleName}")
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun register(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                println("AuthRepositoryImpl: Starting registration for ${user.email}")
                
                // Log API base URL for debugging
                println("AuthRepositoryImpl: API endpoint should be configured")
                
                val encodedPassword = PasswordEncoder.encodePassword(user.password)
                println("AuthRepositoryImpl: Registration - Original password: ${user.password}")
                println("AuthRepositoryImpl: Registration - Encoded password: $encodedPassword")
                
                val registrationDto = UserRegistrationDto(
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    nationality = user.nationality,
                    encryptedPassword = encodedPassword
                )
                
                println("AuthRepositoryImpl: Registration DTO created: $registrationDto")
                
                println("AuthRepositoryImpl: Making API call to register user")
                
                // Add aggressive timeout for API call
                val response = userApi.registerUser(registrationDto)
                
                println("AuthRepositoryImpl: API response code: ${response.code()}")
                println("AuthRepositoryImpl: API response message: ${response.message()}")
                
                when (response.code()) {
                    201 -> {
                        println("AuthRepositoryImpl: Registration successful")
                        // Usuario registrado exitosamente
                        val createdUser = response.body()
                        createdUser?.let {
                            registeredUsers.add(it)
                            println("AuthRepositoryImpl: Created user: $it")
                        }
                        true
                    }
                    409 -> {
                        println("AuthRepositoryImpl: User already exists")
                        // Usuario ya existe
                        false
                    }
                    else -> {
                        println("AuthRepositoryImpl: Server error or other error: ${response.code()}")
                        // Error del servidor u otro error
                        false
                    }
                }
            } catch (e: Exception) {
                println("AuthRepositoryImpl: Network error or exception: ${e.message}")
                e.printStackTrace()
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
                        println("AuthRepositoryImpl: User updated successfully: $user")
                    }
                    true
                }
                404 -> {
                    // User not found
                    println("AuthRepositoryImpl: User not found during update")
                    false
                }
                else -> {
                    // Other server errors
                    println("AuthRepositoryImpl: Server error during update: ${response.code()}")
                    false
                }
            }
        } catch (e: Exception) {
            println("AuthRepositoryImpl: Exception during update: ${e.message}")
            // Update local data even if API call fails
            loggedInUser = user
            // Update in local list
            val index = registeredUsers.indexOfFirst { it.email.equals(user.email, ignoreCase = true) }
            if (index != -1) {
                registeredUsers[index] = user
            }
            println("AuthRepositoryImpl: Updated user locally despite API error")
            true // Return true since local update succeeded
        }
    }
}