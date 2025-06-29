package com.luciaaldana.eccomerceapp.di

import com.luciaaldana.eccomerceapp.model.repository.OrderHistoryRepository
import com.luciaaldana.eccomerceapp.model.repository.OrderHistoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {

    @Provides
    @Singleton
    fun provideOrderHistoryRepository(): OrderHistoryRepository =
        OrderHistoryRepositoryImpl()
}