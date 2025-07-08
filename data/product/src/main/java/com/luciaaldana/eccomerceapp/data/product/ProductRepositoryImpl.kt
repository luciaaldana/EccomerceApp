package com.luciaaldana.eccomerceapp.data.product

import com.luciaaldana.eccomerceapp.data.product.network.ProductApi
import com.luciaaldana.eccomerceapp.data.product.mapper.toDomain
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): List<Product> =
        api.getProducts().map { it.toDomain() }
}