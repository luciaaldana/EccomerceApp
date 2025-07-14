package com.luciaaldana.eccomerceapp.data.auth.di

import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import com.luciaaldana.eccomerceapp.data.auth.AuthRepositoryImpl
import com.luciaaldana.eccomerceapp.data.auth.network.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}