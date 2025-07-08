package com.luciaaldana.eccomerceapp.data.product.di

import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import com.luciaaldana.eccomerceapp.data.product.ProductRepositoryImpl
import com.luciaaldana.eccomerceapp.data.product.network.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository = ProductRepositoryImpl(productApi)
}