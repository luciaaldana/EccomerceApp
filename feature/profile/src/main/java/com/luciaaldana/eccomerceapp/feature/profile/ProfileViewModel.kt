package com.luciaaldana.eccomerceapp.feature.profile

import androidx.lifecycle.ViewModel
import com.luciaaldana.eccomerceapp.core.model.MockUser
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private var currentUser: MockUser? = null

    init {
        currentUser = authRepository.getCurrentUser()
        println("Perfil cargado: ${currentUser?.email} - ${currentUser?.fullName}")
        currentUser?.let {
            _name.value = it.fullName
            _email.value = it.email
        }
    }

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun saveProfile() {
        currentUser?.let {
            val updated = it.copy(fullName = name.value, email = email.value)
            authRepository.updateCurrentUser(updated)
        }
    }

    fun logout() {
        authRepository.logout()
    }
}