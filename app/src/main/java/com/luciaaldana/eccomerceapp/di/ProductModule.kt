package com.luciaaldana.eccomerceapp.di

import com.luciaaldana.eccomerceapp.data.network.ProductApi
import com.luciaaldana.eccomerceapp.model.repository.ProductRepository
import com.luciaaldana.eccomerceapp.model.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}