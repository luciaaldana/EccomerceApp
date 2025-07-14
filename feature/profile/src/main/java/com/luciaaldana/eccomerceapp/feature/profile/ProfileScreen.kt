package com.luciaaldana.eccomerceapp.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileImagePicker

@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()

    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val email by viewModel.email.collectAsState()
    val nationality by viewModel.nationality.collectAsState()
    val userImageUrl by viewModel.userImageUrl.collectAsState()
    val userId by viewModel.userId.collectAsState()
    val createdAt by viewModel.createdAt.collectAsState()
    val isUpdating by viewModel.isUpdating.collectAsState()
    val updateMessage by viewModel.updateMessage.collectAsState()
    val isUploadingImage by viewModel.isUploadingImage.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Header(title = "Mi perfil", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            ProfileImagePicker(
                imageUrl = userImageUrl,
                isUploading = isUploadingImage,
                showEditIndicator = isEditing,
                onImageSelected = { uri ->
                    if (isEditing) {
                        viewModel.uploadImageToCloudinary(uri)
                    }
                }
            )
            
            // User ID and creation date (read-only info)
            userId?.let {
                Text(
                    text = "ID: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            createdAt?.let {
                Text(
                    text = "Miembro desde: ${formatDate(it)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Show update message
            updateMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.contains("exitosamente")) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = if (message.contains("exitosamente")) 
                            MaterialTheme.colorScheme.onPrimaryContainer 
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearUpdateMessage()
                }
            }
            
            if (isEditing) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = viewModel::onFirstNameChanged,
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = viewModel::onLastNameChanged,
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nationality,
                    onValueChange = viewModel::onNationalityChanged,
                    label = { Text("Nacionalidad") },
                    modifier = Modifier.fillMaxWidth()
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.saveProfile()
                            isEditing = false
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isUpdating
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar cambios")
                        }
                    }

                    OutlinedButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileInfoItem("Nombre", firstName)
                    ProfileInfoItem("Apellido", lastName)
                    ProfileInfoItem("Email", email)
                    ProfileInfoItem("Nacionalidad", nationality)
                }

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar perfil")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}


@Composable
private fun ProfileInfoItem(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Simple date formatting - you can enhance this with proper date parsing
        dateString.substring(0, 10) // Extract YYYY-MM-DD from ISO string
    } catch (e: Exception) {
        dateString
    }
}