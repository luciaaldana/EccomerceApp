package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.luciaaldana.eccomerceapp.ui.components.Header
import com.luciaaldana.eccomerceapp.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,
    navController: NavController,
    onAddToCart: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.find { it.id == productId }

    if (product != null) {
        Scaffold(
            topBar = {
                Header(title = "Detalle", navController = navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = product.description)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Precio: ${product.price} $")

                if (product.includesDrink) {
                    Text(
                        text = "Incluye bebida 🥤",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    } else {
        Text("Producto no encontrado")
    }
}
