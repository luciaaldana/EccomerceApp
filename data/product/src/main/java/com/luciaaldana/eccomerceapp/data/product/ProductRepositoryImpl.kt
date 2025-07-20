package com.luciaaldana.eccomerceapp.data.product

import com.luciaaldana.eccomerceapp.data.product.network.ProductApi
import com.luciaaldana.eccomerceapp.data.product.mapper.toDomain
import com.luciaaldana.eccomerceapp.data.product.mapper.toEntity
import com.luciaaldana.eccomerceapp.data.database.dao.ProductDao
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return productDao.getProductById(productId)?.toDomain()
    }

    override suspend fun syncProductsFromApi(): List<Product> {
        val products = api.getProducts().map { it.toDomain() }
        
        productDao.clearAllProducts()
        productDao.insertProducts(products.map { it.toEntity() })
        
        return products
    }

    override suspend fun refreshProducts(): List<Product> {
        return syncProductsFromApi()
    }

    override suspend fun hasLocalProducts(): Boolean {
        return productDao.getProductCount() > 0
    }
}