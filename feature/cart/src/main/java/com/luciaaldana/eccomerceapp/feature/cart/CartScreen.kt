package com.luciaaldana.eccomerceapp.feature.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.ModernCartItemCard
import com.luciaaldana.eccomerceapp.core.ui.components.CartTotalSection
import com.luciaaldana.eccomerceapp.core.ui.components.EmptyStateSection
import com.luciaaldana.eccomerceapp.core.ui.components.ScreenLoadingState

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel
) {
    val items by viewModel.cartItems.collectAsState()
    val total by viewModel.totalAmount.collectAsState()
    val isScreenLoading by viewModel.isScreenLoading.collectAsState()

    val itemCount = items.count()
    val totalUnits = items.sumOf { it.quantity }

    if (isScreenLoading) {
        Scaffold(
            topBar = {
                Header(title = "Carrito de compras", navController = navController)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            ScreenLoadingState(
                modifier = Modifier.padding(innerPadding),
                message = "Cargando carrito..."
            )
        }
        return
    }

    Scaffold(
        topBar = {
            Header(title = "Carrito de compras", navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (items.isEmpty()) {
                // Empty cart state
                EmptyStateSection(
                    icon = Icons.Default.ShoppingCart,
                    title = "Tu carrito está vacío",
                    subtitle = "¡Agrega algunos productos y aparecerán aquí!",
                    buttonText = "Explorar Productos",
                    onButtonClick = { 
                        navController.navigate(route = "productList") {
                            popUpTo(route = "cart") { inclusive = false }
                        }
                    }
                )
            } else {
                // Cart content
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // My Bag header section
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Mi Carrito",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "$itemCount artículos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Cart items list
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = items,
                            key = { item -> item.product.id }
                        ) { item ->
                            ModernCartItemCard(
                                item = item,
                                onQuantityChange = { qty ->
                                    viewModel.updateQuantity(item.product, qty)
                                },
                                onRemove = {
                                    viewModel.remove(item.product)
                                },
                                onToggleFavorite = {
                                    // TODO: Implement favorite functionality
                                }
                            )
                        }
                    }

                    // Total section and place order button
                    CartTotalSection(
                        total = total,
                        onPlaceOrder = {
                            viewModel.confirmOrder()
                            navController.navigate(route = "orderConfirmation")
                        }
                    )
                }
            }
        }
    }
}

