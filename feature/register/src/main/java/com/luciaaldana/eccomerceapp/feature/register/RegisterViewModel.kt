package com.luciaaldana.eccomerceapp.feature.register

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.model.MockUser
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var fullName by mutableStateOf<String>(value = "")
    var email by mutableStateOf<String>(value = "")
    var password by mutableStateOf<String>(value = "")
    var confirmPassword by mutableStateOf<String>(value = "")

    var errorMessage by mutableStateOf<String?>(value = null)
    var isRegistered by mutableStateOf<Boolean>(value = false)

    fun onRegisterClick() {
        if(!isValid()) return

        viewModelScope.launch {
            val user = MockUser(email = email.trim(), password = password, fullName = fullName.trim())
            val success = authRepository.register(user)

            if(success) {
                isRegistered = true
                errorMessage = null
            } else {
                errorMessage = "El usuario ya está registrado"
            }
        }
    }

    private fun isValid(): Boolean {
        if (fullName.isBlank()) {
            errorMessage = "El nombre completo es obligatorio"
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