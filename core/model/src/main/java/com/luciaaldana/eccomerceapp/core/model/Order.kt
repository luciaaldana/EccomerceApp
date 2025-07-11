package com.luciaaldana.eccomerceapp.core.model

import java.util.Date
import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val items: List<CartItem>,
    val total: Double,
    val date: Date = Date()
)