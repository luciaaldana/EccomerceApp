package com.luciaaldana.eccomerceapp.data.cart.mapper

import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.model.Order
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.data.cart.dto.OrderDto
import com.luciaaldana.eccomerceapp.data.cart.dto.OrderResponse
import com.luciaaldana.eccomerceapp.data.cart.dto.ProductOrderDto
import java.util.Date

fun List<CartItem>.toOrderDto(): OrderDto {
    return OrderDto(
        orderId = "ORD-${System.currentTimeMillis()}-${(100000..999999).random()}",
        productIds = this.map { cartItem ->
            ProductOrderDto(
                name = cartItem.product.name,
                description = cartItem.product.description,
                imageUrl = cartItem.product.imageUrl,
                price = cartItem.product.price,
                hasDrink = cartItem.product.includesDrink,
                quantity = cartItem.quantity
            )
        },
        total = this.sumOf { it.product.price * it.quantity },
        timestamp = System.currentTimeMillis()
    )
}

fun OrderResponse.toOrder(): Order {
    return Order(
        id = this.orderId,
        items = this.productIds.map { productDto ->
            CartItem(
                product = Product(
                    id = "",
                    name = productDto.name,
                    description = productDto.description,
                    price = productDto.price,
                    imageUrl = productDto.imageUrl,
                    category = "",
                    includesDrink = productDto.hasDrink
                ),
                quantity = productDto.quantity
            )
        },
        total = this.total,
        date = Date(this.timestamp)
    )
}