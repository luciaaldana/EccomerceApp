package com.luciaaldana.eccomerceapp.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.luciaaldana.eccomerceapp.feature.profile.R
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileImagePicker
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileUserHeader
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileMenuOption
import com.luciaaldana.eccomerceapp.core.ui.components.ProfileEditDialog
import com.luciaaldana.eccomerceapp.core.ui.components.ThemeDropdownMenu
import com.luciaaldana.eccomerceapp.core.ui.components.ScreenLoadingState
import com.luciaaldana.eccomerceapp.core.ui.theme.ThemeMode

@Composable
fun ProfileScreen(navController: NavController) {
    // Simplified version to debug crash
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
    val isScreenLoading by viewModel.isScreenLoading.collectAsState()
    
    val currentThemeMode by viewModel.currentThemeMode.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    if (isScreenLoading) {
        Scaffold(
            topBar = {
                Header(title = stringResource(R.string.profile_title), navController = navController)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            ScreenLoadingState(
                modifier = Modifier.padding(innerPadding),
                message = stringResource(R.string.profile_loading)
            )
        }
        return
    }

    Scaffold(
        topBar = {
            Header(title = stringResource(R.string.profile_title), navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header del usuario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen de perfil
                    ProfileImagePicker(
                        imageUrl = userImageUrl,
                        isUploading = isUploadingImage,
                        showEditIndicator = true,
                        onImageSelected = { uri ->
                            viewModel.uploadImageToCloudinary(uri)
                        },
                        modifier = Modifier.size(100.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Nombre del usuario
                    Text(
                        text = "$firstName $lastName",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Email del usuario
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Mostrar mensaje de actualización
            updateMessage?.let { message ->
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = if (message.contains("exitosamente")) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.clearUpdateMessage()
                }
            }

            // Información del usuario
            userId?.let { id ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile_account_info),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.profile_user_id),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = id.take(8) + "...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        createdAt?.let { date ->
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = stringResource(R.string.profile_member_since),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatDate(date),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Sección de opciones del menú
            Text(
                text = stringResource(R.string.profile_my_options),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Opción de historial de pedidos
            ProfileMenuOption(
                title = stringResource(R.string.profile_order_history),
                subtitle = stringResource(R.string.profile_order_history_subtitle),
                icon = Icons.Default.Receipt,
                onClick = {
                    navController.navigate("orderHistory")
                }
            )

            // Opción de configuración de perfil
            ProfileMenuOption(
                title = stringResource(R.string.profile_edit_info),
                subtitle = stringResource(R.string.profile_edit_info_subtitle),
                icon = Icons.Default.Person,
                onClick = { showEditDialog = true }
            )

            // Selector de tema sin card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Tema",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = stringResource(R.string.profile_app_theme),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Dropdown menu alineado a la izquierda
                ThemeDropdownMenu(
                    currentTheme = currentThemeMode,
                    onThemeSelected = { viewModel.setThemeMode(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cerrar sesión
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
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = stringResource(R.string.profile_logout_description),
                    modifier = Modifier.size(18.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = stringResource(R.string.profile_logout_button),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }

    // Dialog de edición
    if (showEditDialog) {
        ProfileEditDialog(
            firstName = firstName,
            lastName = lastName,
            email = email,
            nationality = nationality,
            isUpdating = isUpdating,
            onFirstNameChange = viewModel::onFirstNameChanged,
            onLastNameChange = viewModel::onLastNameChanged,
            onEmailChange = viewModel::onEmailChanged,
            onNationalityChange = viewModel::onNationalityChanged,
            onSave = {
                viewModel.saveProfile()
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
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

@Composable
private fun ThemeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) 
                MaterialTheme.colorScheme.onPrimary 
            else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
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