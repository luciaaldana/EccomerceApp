package com.luciaaldana.eccomerceapp.feature.register

import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.cloudinary.CloudinaryService
import com.luciaaldana.eccomerceapp.core.model.User
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cloudinaryService: CloudinaryService
): ViewModel() {

    var firstName by mutableStateOf<String>(value = "")
    var lastName by mutableStateOf<String>(value = "")
    var email by mutableStateOf<String>(value = "")
    var password by mutableStateOf<String>(value = "")
    var confirmPassword by mutableStateOf<String>(value = "")
    var nationality by mutableStateOf<String>(value = "")
    var userImageUrl by mutableStateOf<String?>(value = null)
    var selectedImageUri by mutableStateOf<Uri?>(value = null)

    var errorMessage by mutableStateOf<String?>(value = null)
    var isRegistered by mutableStateOf<Boolean>(value = false)
    var isLoading by mutableStateOf<Boolean>(value = false)
    var isUploadingImage by mutableStateOf<Boolean>(value = false)

    fun onRegisterClick() {
        if(!isValid()) return
        if(isLoading) return // Prevent multiple calls

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                
                // Upload image first if selected
                var finalImageUrl: String? = null
                if (selectedImageUri != null) {
                    try {
                        isUploadingImage = true
                        finalImageUrl = cloudinaryService.uploadImage(selectedImageUri!!)
                    } catch (e: Exception) {
                        // If image upload fails, continue without image
                        finalImageUrl = null
                        // Don't set errorMessage here, let registration continue
                    } finally {
                        isUploadingImage = false
                    }
                }
                
                
                val user = User(
                    firstName = firstName.trim(), 
                    lastName = lastName.trim(), 
                    email = email.trim(), 
                    password = password, 
                    nationality = nationality.trim(),
                    userImageUrl = finalImageUrl
                )
                
                val success = authRepository.register(user)

                if(success) {
                    isRegistered = true
                    errorMessage = null
                } else {
                    errorMessage = "Error al registrar usuario. Verifica que el email no esté ya registrado o intenta más tarde." // TODO: Use R.string.register_user_error
                }
            } catch (e: Exception) {
                errorMessage = "Error al registrar usuario. Verifica la conexión e intenta nuevamente." // TODO: Use R.string.register_network_error
                isRegistered = false
            } finally {
                isLoading = false
                isUploadingImage = false
            }
        }
    }

    private fun isValid(): Boolean {
        if (firstName.isBlank()) {
            errorMessage = "El nombre es obligatorio" // TODO: Use R.string.register_name_required
            return false
        }
        if (lastName.isBlank()) {
            errorMessage = "El apellido es obligatorio" // TODO: Use R.string.register_lastname_required
            return false
        }
        if (nationality.isBlank()) {
            errorMessage = "La nacionalidad es obligatoria" // TODO: Use R.string.register_nationality_required
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
           errorMessage = "Email inválido" // TODO: Use R.string.register_invalid_email
           return false
        }
        if (password.length < 8) {
            errorMessage = "La contraseña debe tener al menos 8 caracteres" // TODO: Use R.string.register_password_length_error
            return false
        }
        if (password != confirmPassword) {
            errorMessage = "Las constraseñas no coinciden" // TODO: Use R.string.register_passwords_mismatch
            return false
        }
        errorMessage = null
        return true
    }
    
    fun onImageSelected(uri: Uri) {
        selectedImageUri = uri
        // Show preview of selected image
        userImageUrl = uri.toString()
    }

}