package com.luciaaldana.eccomerceapp.data.product.di

import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import com.luciaaldana.eccomerceapp.data.product.ProductRepositoryImpl
import com.luciaaldana.eccomerceapp.data.product.network.ProductApi
import com.luciaaldana.eccomerceapp.data.product.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    @Named("product_retrofit")
    fun provideProductRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideProductApi(@Named("product_retrofit") retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository = ProductRepositoryImpl(productApi)
}