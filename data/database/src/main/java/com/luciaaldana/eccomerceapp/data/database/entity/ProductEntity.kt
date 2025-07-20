package com.luciaaldana.eccomerceapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val includesDrink: Boolean,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)