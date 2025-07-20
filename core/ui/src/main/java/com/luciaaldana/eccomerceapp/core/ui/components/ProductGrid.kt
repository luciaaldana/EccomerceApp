package com.luciaaldana.eccomerceapp.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luciaaldana.eccomerceapp.core.model.Product

fun LazyListScope.ProductGrid(
    products: List<Product>,
    isLoggedIn: Boolean,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    horizontalPadding: Int = 0
) {
    items(products.chunked(2)) { rowProducts ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowProducts.forEach { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { onAddToCart(product) },
                    onClick = { onProductClick(product) },
                    modifier = Modifier.weight(1f),
                    isLoggedIn = isLoggedIn
                )
            }
            
            // Fill remaining space if odd number of products in last row
            if (rowProducts.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}