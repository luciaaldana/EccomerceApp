package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.utils.toPriceFormat
import com.luciaaldana.eccomerceapp.ui.components.Header
import com.luciaaldana.eccomerceapp.viewmodel.*

@Composable
fun OrderConfirmationScreen(navController: NavController) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val orderViewModel: OrderHistoryViewModel = hiltViewModel()

    val cartItems by cartViewModel.cartItems.collectAsState()
    val total by cartViewModel.totalAmount.collectAsState()

    Scaffold(
        topBar = {
            Header(title = "Confirmar pedido", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Text("Tu carrito está vacío.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("productList") }) {
                    Text("Ir al catálogo")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${item.quantity} x ${item.product.name}")
                            Text("${(item.product.price * item.quantity).toPriceFormat()}")

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total:", style = MaterialTheme.typography.titleLarge)
                    Text("${total.toPriceFormat()}", style = MaterialTheme.typography.titleLarge)

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        orderViewModel.confirmOrder()
                        navController.navigate("orderHistory")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar compra")
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Seguir editando")
                }
            }
        }
    }
}
