package com.luciaaldana.eccomerceapp.data.database.dao

import androidx.room.*
import com.luciaaldana.eccomerceapp.data.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?
    
    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)
    
    @Query("DELETE FROM products")
    suspend fun clearAllProducts()
    
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
    
    @Query("SELECT MAX(lastSyncTimestamp) FROM products")
    suspend fun getLastSyncTimestamp(): Long?
}