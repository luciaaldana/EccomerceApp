package com.luciaaldana.eccomerceapp.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.luciaaldana.eccomerceapp.core.model.utils.toPriceFormat
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.feature.cart.CartViewModel
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository

@Composable
fun ProductListScreen(navController: NavController) {
    val viewModel: ProductsViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()
    val products by viewModel.filteredProducts.collectAsState()
    val all by viewModel.allProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val searchText by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categories = remember (all) {
        all.map { it.category }
            .distinct()
            .filter { it.isNotBlank() }
            .sorted()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text(text = "Buscar producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CategoryDropdown(
            categories = categories,
            selected = selectedCategory,
            onSelected = { viewModel.onCategorySelected(it)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = error ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retryLoadProducts() }) {
                        Text("Reintentar")
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { 
                                if (viewModel.isUserLoggedIn()) {
                                    cartViewModel.add(product)
                                } else {
                                    navController.navigate("login")
                                }
                            },
                            onClick = {
                                navController.navigate("detail/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    categories: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(value = false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = selected ?: "Todas las categorías" )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text="Todas") }, onClick = {
                onSelected(null)
                expanded = false
            })
            categories.forEach { category ->
                DropdownMenuItem(text = { Text(text=category) }, onClick = {
                    onSelected(category)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
            .height(300.dp)
            .clickable(onClick = onClick),
        shape = RectangleShape
    ) {
        Column(modifier = Modifier.padding(0.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "${product.price.toPriceFormat()}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (product.includesDrink) {
                        Text(
                            text = "* Incluye bebida 🥤",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    }
}