package com.luciaaldana.eccomerceapp.feature.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.luciaaldana.eccomerceapp.core.model.utils.toPriceFormat
import com.luciaaldana.eccomerceapp.core.model.CartItem
import com.luciaaldana.eccomerceapp.core.ui.components.Header

@Composable
fun CartScreen(navController: NavController) {
    val viewModel: CartViewModel = hiltViewModel()
    val items by viewModel.cartItems.collectAsState()
    val total by viewModel.totalAmount.collectAsState()

    val itemCount = items.count()
    val totalUnits = items.sumOf { it.quantity }

    Scaffold(
        topBar = {
            Header(title = "Carrito de compras", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Tu carrito está vacío")
                    Button(
                        onClick = { navController.navigate(route = "productList") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seguir comprando")
                    }
                }

            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items) { item ->
                        CartItemCard(item, onUpdate = { qty ->
                            viewModel.updateQuantity(item.product, qty)
                        }, onRemove = {
                            viewModel.remove(item.product)
                        })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Total", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "${total.toPriceFormat()}",
                            style = MaterialTheme.typography.titleLarge
                        )

                    }
                    Text(
                        text = "$itemCount productos",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "$totalUnits unidades",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "Vaciar carrito",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .clickable { viewModel.clearCart() }
                            .padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate(route = "productList") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir comprando")
                }
                Button(
                    onClick = { navController.navigate(route = "orderConfirmation") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = items.isNotEmpty()
                ) {
                    Text("Confirmar compra")
                }

            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItem,
    onUpdate: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row (
            modifier = Modifier
                .padding(16.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(item.product.imageUrl),
                contentDescription = item.product.name,
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column( modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = item.product.description,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Eliminar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .clickable { onRemove() }
                            .padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {
                            if (item.quantity > 1) {
                                onUpdate(item.quantity - 1)
                            } else {
                                onRemove()
                            }
                            },
                            modifier = Modifier
                                .padding(2.dp)
                                .size(24.dp),
                            contentPadding = PaddingValues(2.dp)
                            ) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${item.quantity}")
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onUpdate(item.quantity + 1) },
                            modifier = Modifier
                                .padding(2.dp)
                                .size(24.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text("+")
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${(item.product.price * item.quantity).toPriceFormat()}")
                }
            }
        }
    }
}