package com.luciaaldana.eccomerceapp.core.cloudinary

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class CloudinaryService @Inject constructor(
    private val context: Context,
    private val cloudinaryConfig: CloudinaryConfig
) {
    
    init {
        val config = hashMapOf<String, String>(
            "cloud_name" to cloudinaryConfig.cloudName,
            "api_key" to cloudinaryConfig.apiKey,
            "api_secret" to cloudinaryConfig.apiSecret
        )
        MediaManager.init(context, config)
    }
    
    suspend fun uploadImage(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .option("folder", "profile_images")
            .option("transformation", "c_thumb,w_300,h_300,g_face,r_max")
            .option("format", "jpg")
            .option("quality", "auto")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Upload started
                }
                
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Progress update
                }
                
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val secureUrl = resultData["secure_url"] as? String
                    if (secureUrl != null) {
                        continuation.resume(secureUrl)
                    } else {
                        continuation.resumeWithException(
                            Exception("No se pudo obtener la URL de la imagen")
                        )
                    }
                }
                
                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(
                        Exception("Error al subir imagen: ${error.description}")
                    )
                }
                
                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Upload rescheduled
                }
            })
            .dispatch()
    }
}