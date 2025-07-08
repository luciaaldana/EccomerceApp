package com.luciaaldana.eccomerceapp.data.product.mapper

import com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
import com.luciaaldana.eccomerceapp.core.model.Product

fun ProductDto.toDomain() = Product(
    id          = id,
    name        = name,
    description = description,
    price       = price,
    imageUrl    = imageUrl,
    category    = category ?: "Sin categoría",
    includesDrink = hasDrink ?: false
)