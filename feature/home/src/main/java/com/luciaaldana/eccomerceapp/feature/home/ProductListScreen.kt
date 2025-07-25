package com.luciaaldana.eccomerceapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luciaaldana.eccomerceapp.feature.home.R
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.feature.cart.CartViewModel
import com.luciaaldana.eccomerceapp.core.ui.components.HomeHeader
import com.luciaaldana.eccomerceapp.core.ui.components.SearchBar
import com.luciaaldana.eccomerceapp.core.ui.components.PromoBanner
import com.luciaaldana.eccomerceapp.core.ui.components.CategoryChips
import com.luciaaldana.eccomerceapp.core.ui.components.ProductGrid
import com.luciaaldana.eccomerceapp.core.ui.components.PrimaryButton
import com.luciaaldana.eccomerceapp.core.ui.components.ScreenLoadingState

@Composable
fun ProductListScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val viewModel: ProductsViewModel = hiltViewModel()
    val products by viewModel.filteredProducts.collectAsState()
    val all by viewModel.allProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isScreenLoading by viewModel.isScreenLoading.collectAsState()
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

    if (isScreenLoading) {
        ScreenLoadingState(message = stringResource(R.string.home_loading))
        return
    }

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
                    navController.navigate("login?returnTo=profile")
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
                    placeholder = stringResource(R.string.home_search_placeholder)
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
                            text = stringResource(R.string.home_categories),
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
                                    text = stringResource(R.string.home_loading),
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
                                    text = stringResource(R.string.home_error_loading),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = error ?: stringResource(R.string.home_error_unknown),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                PrimaryButton(
                                    text = stringResource(R.string.home_retry_button),
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
                                        text = stringResource(R.string.home_no_products),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (searchText.isNotEmpty()) {
                                            stringResource(R.string.home_search_help)
                                        } else {
                                            stringResource(R.string.home_try_later)
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
                                stringResource(R.string.home_search_results) + "${products.size})"
                            } else if (selectedCategory != null) {
                                "$selectedCategory (${products.size})"
                            } else {
                                stringResource(R.string.home_all_products) + "${products.size})"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }

            // Products grid usando componente reutilizable
            if (!isLoading && error == null && products.isNotEmpty()) {
                ProductGrid(
                    products = products,
                    isLoggedIn = isLoggedIn,
                    onProductClick = { product ->
                        navController.navigate("detail/${product.id}")
                    },
                    onAddToCart = { product ->
                        if (isLoggedIn) {
                            cartViewModel.add(product)
                        } else {
                            navController.navigate("login?returnTo=productList")
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}