package com.luciaaldana.eccomerceapp.data.network.mapper

import com.luciaaldana.eccomerceapp.data.network.dto.ProductDto
import com.luciaaldana.eccomerceapp.model.data.Product

fun ProductDto.toDomain() = Product(
    id          = id,
    name        = name,
    description = description,
    price       = price,
    imageUrl    = imageUrl,
    category    = "default",     // ← ajusta si tu API envía categoría
    includesDrink = hasDrink
)
