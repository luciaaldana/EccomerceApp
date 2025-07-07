package com.luciaaldana.eccomerceapp.feature.login

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun onLoginClick() {
        viewModelScope.launch {
            if (!isValidEmail(email)) {
                errorMessage = "Email inválido"
                return@launch
            }

            if (password.length < 8) {
                errorMessage = "La constraseña debe tener al menos 8 caracteres"
                return@launch
            }

            val success = authRepository.login(email.trim(), password)

            if (success) {
                isLoggedIn = true
                errorMessage = null
            } else {
                errorMessage = "Credenciales incorrectas"
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