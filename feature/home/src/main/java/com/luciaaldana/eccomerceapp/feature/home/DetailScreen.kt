package com.luciaaldana.eccomerceapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.ProductHeroImage
import com.luciaaldana.eccomerceapp.core.ui.components.ProductInfoSection
import com.luciaaldana.eccomerceapp.core.ui.components.QuantityInput
import com.luciaaldana.eccomerceapp.core.ui.components.AddToCartBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: String,
    navController: NavController,
    onAddToCart: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.find { it.id == productId }
    
    var quantity by remember { mutableStateOf("1") }

    if (product != null) {
        // Create dynamic title based on category
        val categoryMessage = when {
            product.category.contains("italiana", ignoreCase = true) -> "Comida italiana para hoy, perfecto!"
            product.category.contains("mexicana", ignoreCase = true) -> "Sabor mexicano auténtico, delicioso!"
            product.category.contains("pizza", ignoreCase = true) -> "Pizza fresca, recién horneada!"
            product.category.contains("hamburguesa", ignoreCase = true) -> "Hamburguesas jugosas, irresistibles!"
            product.category.contains("postre", ignoreCase = true) -> "Dulce tentación, el final perfecto!"
            product.category.contains("bebida", ignoreCase = true) -> "Refrescante y delicioso!"
            else -> "${product.category.replaceFirstChar { it.uppercase() }}, simplemente delicioso!"
        }
        
        Scaffold(
            topBar = {
                Header(title = categoryMessage, navController = navController)
            },
            bottomBar = {
                val quantityInt = quantity.toIntOrNull() ?: 1
                AddToCartBottomBar(
                    quantity = quantityInt,
                    unitPrice = product.price,
                    onAddToCart = {
                        repeat(quantityInt) {
                            onAddToCart()
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Hero image
                item {
                    ProductHeroImage(
                        imageUrl = product.imageUrl,
                        productName = product.name,
                        includesDrink = product.includesDrink
                    )
                }
                
                // Product info section
                item {
                    ProductInfoSection(
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        category = product.category,
                        includesDrink = product.includesDrink
                    )
                }
                
                // Quantity input
                item {
                    QuantityInput(
                        quantity = quantity,
                        onQuantityChange = { newQuantity ->
                            if (newQuantity.isEmpty() || (newQuantity.toIntOrNull() ?: 0) >= 1) {
                                quantity = newQuantity
                            }
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Producto no encontrado",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "El producto que buscas no existe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}