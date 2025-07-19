package com.luciaaldana.eccomerceapp.feature.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.cloudinary.CloudinaryService
import com.luciaaldana.eccomerceapp.core.model.User
import com.luciaaldana.eccomerceapp.core.ui.theme.ThemeMode
import com.luciaaldana.eccomerceapp.core.ui.theme.ThemeRepository
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cloudinaryService: CloudinaryService,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _nationality = MutableStateFlow("")
    val nationality: StateFlow<String> = _nationality.asStateFlow()

    private val _userImageUrl = MutableStateFlow<String?>(null)
    val userImageUrl: StateFlow<String?> = _userImageUrl.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _createdAt = MutableStateFlow<String?>(null)
    val createdAt: StateFlow<String?> = _createdAt.asStateFlow()

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()
    
    private val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading.asStateFlow()

    private val _updateMessage = MutableStateFlow<String?>(null)
    val updateMessage: StateFlow<String?> = _updateMessage.asStateFlow()

    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage: StateFlow<Boolean> = _isUploadingImage.asStateFlow()

    val currentThemeMode: StateFlow<ThemeMode> = themeRepository.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.SYSTEM
    )

    private var currentUser: User? = null

    init {
        currentUser = authRepository.getCurrentUser()
        currentUser?.let {
            _firstName.value = it.firstName
            _lastName.value = it.lastName
            _email.value = it.email
            _nationality.value = it.nationality
            _userImageUrl.value = it.userImageUrl
            _userId.value = it._id
            _createdAt.value = it.createdAt
        }
        simulateScreenLoading()
    }
    
    private fun simulateScreenLoading() {
        viewModelScope.launch {
            delay(700) // Simula tiempo de carga de screen
            _isScreenLoading.value = false
        }
    }

    fun onFirstNameChanged(newFirstName: String) {
        _firstName.value = newFirstName
    }

    fun onLastNameChanged(newLastName: String) {
        _lastName.value = newLastName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onNationalityChanged(newNationality: String) {
        _nationality.value = newNationality
    }

    fun onUserImageUrlChanged(newImageUrl: String) {
        _userImageUrl.value = newImageUrl
    }
    
    fun uploadImageToCloudinary(uri: Uri) {
        viewModelScope.launch {
            try {
                _isUploadingImage.value = true
                _updateMessage.value = null
                
                val cloudinaryUrl = cloudinaryService.uploadImage(uri)
                _userImageUrl.value = cloudinaryUrl
                _updateMessage.value = "Imagen subida exitosamente"
                
            } catch (e: Exception) {
                _updateMessage.value = "Error al subir imagen: ${e.message}"
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    fun saveProfile() {
        currentUser?.let { user ->
            viewModelScope.launch {
                _isUpdating.value = true
                _updateMessage.value = null
                
                val updated = user.copy(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    nationality = nationality.value,
                    userImageUrl = userImageUrl.value
                )
                
                val success = authRepository.updateUserProfile(updated)
                
                _isUpdating.value = false
                
                if (success) {
                    _updateMessage.value = "Perfil actualizado exitosamente"
                    // Update current user reference
                    currentUser = authRepository.getCurrentUser()
                } else {
                    _updateMessage.value = "Error al actualizar el perfil. Inténtalo de nuevo."
                }
            }
        }
    }
    
    fun clearUpdateMessage() {
        _updateMessage.value = null
    }

    fun logout() {
        authRepository.logout()
    }
    
    fun getCurrentUser(): User? {
        return authRepository.getCurrentUser()
    }
    
    fun setThemeMode(themeMode: ThemeMode) {
        themeRepository.setThemeMode(themeMode)
    }
}