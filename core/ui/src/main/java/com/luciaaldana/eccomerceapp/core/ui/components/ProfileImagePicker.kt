package com.luciaaldana.eccomerceapp.core.ui.components

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileImagePicker(
    imageUrl: String?,
    isUploading: Boolean,
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier,
    showEditIndicator: Boolean = false
) {
    val context = LocalContext.current
    var showImagePicker by remember { mutableStateOf(false) }
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }
    
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraImageUri.value?.let { uri ->
                onImageSelected(uri)
            }
        }
    }
    
    Box(
        modifier = modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .diskCacheKey(imageUrl)
                    .build(),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { showImagePicker = true },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_camera),
                error = painterResource(android.R.drawable.ic_menu_camera)
            )
        } else {
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { showImagePicker = true },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar foto",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Foto",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        if (isUploading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        if (showEditIndicator && imageUrl != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { showImagePicker = true },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar foto",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
    
    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("Elige una opción para tu foto de perfil") },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            galleryLauncher.launch("image/*")
                            showImagePicker = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Galería")
                    }
                    
                    TextButton(
                        onClick = {
                            if (cameraPermissionState.status.isGranted) {
                                val photoFile = createImageFile(context)
                                val photoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    photoFile
                                )
                                cameraImageUri.value = photoUri
                                cameraLauncher.launch(photoUri)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                            showImagePicker = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cámara")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImagePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir = File(context.cacheDir, "images")
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}