package com.luciaaldana.eccomerceapp.data.cart.di

import com.luciaaldana.eccomerceapp.domain.cart.CartItemRepository
import com.luciaaldana.eccomerceapp.domain.cart.OrderHistoryRepository
import com.luciaaldana.eccomerceapp.data.cart.CartItemRepositoryImpl
import com.luciaaldana.eccomerceapp.data.cart.OrderHistoryRepositoryImpl
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
    fun provideCartItemRepository(): CartItemRepository = CartItemRepositoryImpl()

    @Provides
    @Singleton
    fun provideOrderHistoryRepository(): OrderHistoryRepository = OrderHistoryRepositoryImpl()
}