package com.luciaaldana.eccomerceapp.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.luciaaldana.eccomerceapp.feature.profile.R
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.OrderHistoryCard
import com.luciaaldana.eccomerceapp.core.ui.components.EmptyStateSection

@Composable
fun OrderHistoryScreen(navController: NavController) {
    val viewModel: OrderHistoryViewModel = hiltViewModel()
    val orders by viewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            Header(title = stringResource(R.string.order_history_title), navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (orders.isEmpty()) {
                // Empty order history state
                EmptyStateSection(
                    icon = Icons.Default.ShoppingBag,
                    title = stringResource(R.string.order_history_empty_title),
                    subtitle = stringResource(R.string.order_history_empty_subtitle),
                    buttonText = stringResource(R.string.order_history_empty_button),
                    onButtonClick = {
                        navController.navigate(route = "productList") {
                            popUpTo(route = "orderHistory") { inclusive = true }
                        }
                    }
                )
            } else {
                // Contenido principal
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header de la sección
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.order_history_my_orders),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "${orders.size} ${if (orders.size == 1) stringResource(R.string.order_history_single_order) else stringResource(R.string.order_history_multiple_orders)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Lista de pedidos
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(orders.sortedByDescending { it.date }) { order ->
                            OrderHistoryCard(order = order)
                        }
                        
                        // Espacio adicional al final
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Botones de acción
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.clearHistory()
                                navController.navigate(route = "productList") {
                                    popUpTo(route = "orderHistory") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = stringResource(R.string.order_history_clear_description),
                                modifier = Modifier.size(18.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = stringResource(R.string.order_history_clear_button),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Button(
                            onClick = {
                                navController.navigate(route = "productList") {
                                    popUpTo(route = "orderHistory") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.order_history_continue_shopping),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}