package com.luciaaldana.eccomerceapp.model.data

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    //TODO: Se podría usar algo como CharCategory para tener types seguros y evitar errores?
    val category: String,
    val includesDrink: Boolean
)