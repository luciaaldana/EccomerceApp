package com.luciaaldana.eccomerceapp.feature.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.model.User
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var firstName by mutableStateOf<String>(value = "")
    var lastName by mutableStateOf<String>(value = "")
    var email by mutableStateOf<String>(value = "")
    var password by mutableStateOf<String>(value = "")
    var confirmPassword by mutableStateOf<String>(value = "")
    var nationality by mutableStateOf<String>(value = "")

    var errorMessage by mutableStateOf<String?>(value = null)
    var isRegistered by mutableStateOf<Boolean>(value = false)
    var isLoading by mutableStateOf<Boolean>(value = false)

    fun onRegisterClick() {
        if(!isValid()) return
        if(isLoading) return // Prevent multiple calls

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                val user = User(
                    firstName = firstName.trim(), 
                    lastName = lastName.trim(), 
                    email = email.trim(), 
                    password = password, 
                    nationality = nationality.trim()
                )
                
                println("Attempting to register user: ${user.email}")
                val success = authRepository.register(user)
                println("Registration result: $success")

                if(success) {
                    isRegistered = true
                    errorMessage = null
                } else {
                    errorMessage = "Error al registrar usuario. Verifica que el email no esté ya registrado o intenta más tarde."
                }
            } catch (e: Exception) {
                println("Registration exception: ${e.message}")
                errorMessage = "Error al registrar usuario. Verifica la conexión e intenta nuevamente."
                isRegistered = false
            } finally {
                isLoading = false
            }
        }
    }

    private fun isValid(): Boolean {
        if (firstName.isBlank()) {
            errorMessage = "El nombre es obligatorio"
            return false
        }
        if (lastName.isBlank()) {
            errorMessage = "El apellido es obligatorio"
            return false
        }
        if (nationality.isBlank()) {
            errorMessage = "La nacionalidad es obligatoria"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
           errorMessage = "Email inválido"
           return false
        }
        if (password.length < 8) {
            errorMessage = "La contraseña debe tener al menos 8 caracteres"
            return false
        }
        if (password != confirmPassword) {
            errorMessage = "Las constraseñas no coinciden"
            return false
        }
        errorMessage = null
        return true
    }

}