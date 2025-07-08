package com.luciaaldana.eccomerceapp.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.model.utils.toReadableFormat
import com.luciaaldana.eccomerceapp.core.model.utils.toRelativeTime
import com.luciaaldana.eccomerceapp.core.ui.components.Header

@Composable
fun OrderHistoryScreen(navController: NavController) {
    val viewModel: OrderHistoryViewModel = hiltViewModel()
    val orders by viewModel.orders.collectAsState()

    Scaffold(
        topBar = {
            Header(title = "Mis compras", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (orders.isEmpty()) {
                Text("No hay pedidos realizados.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders.sortedByDescending { it.date }) { order ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "${order.id.take(8)}...",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text("${order.date.toRelativeTime()}")
                                Spacer(modifier = Modifier.height(8.dp))
                                order.items.forEach {
                                    Text("${it.quantity}x ${it.product.name} - \$${it.product.price * it.quantity}")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Total: \$${order.total}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.clearHistory()
                        navController.navigate(route = "productList") {
                            popUpTo(route = "orderHistory") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Limpiar historial")
                }
                Button(
                    onClick = {
                        navController.navigate(route = "productList") {
                            popUpTo(route = "orderHistory") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir comprando")
                }
            }
        }
    }
}