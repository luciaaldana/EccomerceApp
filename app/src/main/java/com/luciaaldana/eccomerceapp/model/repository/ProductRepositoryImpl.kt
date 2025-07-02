package com.luciaaldana.eccomerceapp.model.repository

import com.luciaaldana.eccomerceapp.data.network.ProductApi
import com.luciaaldana.eccomerceapp.data.network.mapper.toDomain
import com.luciaaldana.eccomerceapp.model.data.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): List<Product> =
        api.getProducts().map { it.toDomain() }
}