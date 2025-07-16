package com.luciaaldana.eccomerceapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.feature.cart.CartViewModel
import com.luciaaldana.eccomerceapp.core.ui.components.HomeHeader
import com.luciaaldana.eccomerceapp.core.ui.components.SearchBar
import com.luciaaldana.eccomerceapp.core.ui.components.PromoBanner
import com.luciaaldana.eccomerceapp.core.ui.components.CategoryChips
import com.luciaaldana.eccomerceapp.core.ui.components.ProductCard
import com.luciaaldana.eccomerceapp.core.ui.components.PrimaryButton

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

    val categories = remember(all) {
        all.map { it.category }
            .distinct()
            .filter { it.isNotBlank() }
            .sorted()
    }

    // Get user info
    val isLoggedIn = viewModel.isUserLoggedIn()
    val currentUser = if (isLoggedIn) {
        viewModel.getCurrentUser()
    } else null
    val userName = currentUser?.firstName
    val userImageUrl = currentUser?.userImageUrl

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        HomeHeader(
            userName = userName,
            userImageUrl = userImageUrl,
            isLoggedIn = isLoggedIn,
            onProfileClick = {
                if (isLoggedIn) {
                    navController.navigate("profile")
                } else {
                    navController.navigate("login")
                }
            }
        )

        // Main content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Search bar
            item {
                SearchBar(
                    query = searchText,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = "Buscar productos..."
                )
            }

            // Promo banner (only show when not searching)
            if (searchText.isEmpty()) {
                item {
                    PromoBanner()
                }
            }

            // Categories
            if (categories.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            text = "Categorías",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        CategoryChips(
                            categories = categories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = { viewModel.onCategorySelected(it) }
                        )
                    }
                }
            }

            // Products section
            item {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Cargando productos...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    error != null -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Error al cargar productos",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = error ?: "Error desconocido",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                PrimaryButton(
                                    text = "Reintentar",
                                    onClick = { viewModel.retryLoadProducts() }
                                )
                            }
                        }
                    }
                    products.isEmpty() -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "No hay productos",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (searchText.isNotEmpty()) {
                                            "Intenta con otros términos de búsqueda"
                                        } else {
                                            "Vuelve más tarde"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Products header
                        Text(
                            text = if (searchText.isNotEmpty()) {
                                "Resultados (${products.size})"
                            } else if (selectedCategory != null) {
                                "$selectedCategory (${products.size})"
                            } else {
                                "Todos los productos (${products.size})"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }

            // Products grid
            if (!isLoading && error == null && products.isNotEmpty()) {
                items(products.chunked(2)) { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = {
                                    if (isLoggedIn) {
                                        cartViewModel.add(product)
                                    } else {
                                        navController.navigate("login")
                                    }
                                },
                                onClick = {
                                    navController.navigate("detail/${product.id}")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        // Fill remaining space if odd number of products in last row
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}