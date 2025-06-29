package com.luciaaldana.eccomerceapp.di


import com.luciaaldana.eccomerceapp.model.repository.AuthRepository
import com.luciaaldana.eccomerceapp.model.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}