package com.luciaaldana.eccomerceapp.data.auth.di

import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import com.luciaaldana.eccomerceapp.data.auth.AuthRepositoryImpl
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
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}