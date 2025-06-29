package com.luciaaldana.eccomerceapp.di

import com.luciaaldana.eccomerceapp.model.repository.ProductRepository
import com.luciaaldana.eccomerceapp.model.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun providesProductRepository(): ProductRepository = ProductRepositoryImpl()
}