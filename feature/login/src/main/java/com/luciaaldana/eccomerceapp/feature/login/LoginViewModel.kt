package com.luciaaldana.eccomerceapp.feature.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject
import android.util.Patterns

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf<String>(value = "")
    var password by mutableStateOf<String>(value = "")
    var isLoggedIn by mutableStateOf<Boolean>(value =  false)
    var errorMessage by mutableStateOf<String?>(value = null)
    var isLoading by mutableStateOf<Boolean>(value = false)

    fun onLoginClick() {
        if (isLoading) return // Prevent multiple calls
        
        viewModelScope.launch {
            if (!isValidEmail(email)) {
                errorMessage = "Email inválido"
                return@launch
            }

            if (password.length < 8) {
                errorMessage = "La constraseña debe tener al menos 8 caracteres"
                return@launch
            }

            try {
                isLoading = true
                errorMessage = null
                
                println("LoginViewModel: Attempting login for ${email}")
                
                val success = authRepository.login(email.trim(), password)

                if (success) {
                    isLoggedIn = true
                    errorMessage = null
                    println("LoginViewModel: Login successful")
                } else {
                    errorMessage = "Error al iniciar sesión. Verifica tu email y contraseña."
                    println("LoginViewModel: Login failed - invalid credentials")
                }
            } catch (e: Exception) {
                errorMessage = "Error al iniciar sesión. Verifica la conexión e intenta nuevamente."
                println("LoginViewModel: Login exception: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        authRepository.logout()
        isLoggedIn = false
        email = ""
        password = ""
        errorMessage = null
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}