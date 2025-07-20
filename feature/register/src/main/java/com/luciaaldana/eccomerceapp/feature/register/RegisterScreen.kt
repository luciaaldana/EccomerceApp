package com.luciaaldana.eccomerceapp.feature.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileImagePicker
import com.luciaaldana.eccomerceapp.core.ui.components.EmailTextField
import com.luciaaldana.eccomerceapp.core.ui.components.PasswordTextField
import com.luciaaldana.eccomerceapp.core.ui.components.NameTextField
import com.luciaaldana.eccomerceapp.core.ui.components.PrimaryButton
import com.luciaaldana.eccomerceapp.core.ui.components.OutlinedButton
import com.luciaaldana.eccomerceapp.core.ui.components.LoginHeader

@Composable
fun RegisterScreen(navController: NavController) {
   val viewModel: RegisterViewModel = hiltViewModel()

    if (viewModel.isRegistered) {
        LaunchedEffect(Unit) {
            navController.navigate(route = "login") {
                popUpTo(route = "register") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // Header
        LoginHeader(
            title = "Crear cuenta",
            subtitle = "Únete a nosotros hoy"
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Registration form
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Profile Image Picker
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileImagePicker(
                        imageUrl = viewModel.userImageUrl,
                        isUploading = viewModel.isUploadingImage,
                        onImageSelected = { uri ->
                            viewModel.onImageSelected(uri)
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Foto de perfil (opcional)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Name fields
                NameTextField(
                    value = viewModel.firstName,
                    onValueChange = { viewModel.firstName = it },
                    placeholder = "Tu nombre",
                    isError = viewModel.errorMessage?.contains("nombre", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("nombre", ignoreCase = true) == true) {
                        "El nombre es requerido"
                    } else null
                )

                NameTextField(
                    value = viewModel.lastName,
                    onValueChange = { viewModel.lastName = it },
                    placeholder = "Tu apellido",
                    isError = viewModel.errorMessage?.contains("apellido", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("apellido", ignoreCase = true) == true) {
                        "El apellido es requerido"
                    } else null
                )

                // Email field
                EmailTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    isError = viewModel.errorMessage?.contains("email", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("email", ignoreCase = true) == true) {
                        "Por favor ingresa un email válido"
                    } else null
                )

                // Password fields
                PasswordTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    placeholder = "Tu contraseña",
                    isError = viewModel.errorMessage?.contains("contraseña", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("contraseña", ignoreCase = true) == true) {
                        "La contraseña debe tener al menos 6 caracteres"
                    } else null
                )

                PasswordTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    placeholder = "Confirmar contraseña",
                    isError = viewModel.errorMessage?.contains("coinciden", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("coinciden", ignoreCase = true) == true) {
                        "Las contraseñas no coinciden"
                    } else null
                )

                NameTextField(
                    value = viewModel.nationality,
                    onValueChange = { viewModel.nationality = it },
                    placeholder = "Tu nacionalidad",
                    isError = viewModel.errorMessage?.contains("nacionalidad", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("nacionalidad", ignoreCase = true) == true) {
                        "La nacionalidad es requerida"
                    } else null
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Register button
                PrimaryButton(
                    text = if (viewModel.isLoading) "Registrando..." else "Crear cuenta",
                    onClick = { viewModel.onRegisterClick() },
                    enabled = !viewModel.isLoading
                )

                // Cancel button
                OutlinedButton(
                    text = "Cancelar",
                    onClick = { 
                        navController.navigate("productList") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }
        }

        // Error message (outside card for better visibility)
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Login link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tenés cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}