package com.luciaaldana.eccomerceapp.di

import android.content.Context
import com.luciaaldana.eccomerceapp.BuildConfig
import com.luciaaldana.eccomerceapp.core.cloudinary.CloudinaryConfig
import com.luciaaldana.eccomerceapp.core.cloudinary.CloudinaryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {
    
    @Provides
    @Singleton
    fun provideCloudinaryConfig(): CloudinaryConfig {
        return CloudinaryConfig(
            cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME,
            apiKey = BuildConfig.CLOUDINARY_API_KEY,
            apiSecret = BuildConfig.CLOUDINARY_API_SECRET
        )
    }
    
    @Provides
    @Singleton
    fun provideCloudinaryService(
        @ApplicationContext context: Context,
        cloudinaryConfig: CloudinaryConfig
    ): CloudinaryService {
        return CloudinaryService(context, cloudinaryConfig)
    }
}