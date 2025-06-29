package com.luciaaldana.eccomerceapp.di

import com.luciaaldana.eccomerceapp.model.repository.CartItemRepository
import com.luciaaldana.eccomerceapp.model.repository.CartItemRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartItemsRepository(): CartItemRepository = CartItemRepositoryImpl()
}