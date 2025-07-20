package com.luciaaldana.eccomerceapp.data.product.mapper

import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.data.database.entity.ProductEntity

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        includesDrink = includesDrink
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        category = category,
        includesDrink = includesDrink,
        lastSyncTimestamp = System.currentTimeMillis()
    )
}