package com.luciaaldana.eccomerceapp.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.luciaaldana.eccomerceapp.feature.home.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.luciaaldana.eccomerceapp.core.ui.components.Header
import com.luciaaldana.eccomerceapp.core.ui.components.ProductHeroImage
import com.luciaaldana.eccomerceapp.core.ui.components.ProductInfoSection
import com.luciaaldana.eccomerceapp.core.ui.components.QuantityBuySection
import com.luciaaldana.eccomerceapp.core.ui.components.ProductGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: String,
    navController: NavController,
    onAddToCart: () -> Unit,
    onAddRelatedToCart: (String) -> Unit = {},
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.find { it.id == productId }
    
    // Filtrar productos excluyendo el actual
    val relatedProducts = allProducts.filter { it.id != productId }
    
    var quantity by remember { mutableIntStateOf(1) }
    var showAddedToCartMessage by remember { mutableStateOf(false) }

    if (product != null) {
        // Create dynamic title based on category
        val categoryMessage = when {
            product.category.contains("italiana", ignoreCase = true) -> stringResource(R.string.detail_italian_food)
            product.category.contains("mexicana", ignoreCase = true) -> stringResource(R.string.detail_mexican_food)
            product.category.contains("pizza", ignoreCase = true) -> stringResource(R.string.detail_pizza)
            product.category.contains("hamburguesa", ignoreCase = true) -> stringResource(R.string.detail_burger)
            product.category.contains("postre", ignoreCase = true) -> stringResource(R.string.detail_dessert)
            product.category.contains("bebida", ignoreCase = true) -> stringResource(R.string.detail_drink)
            else -> "${product.category.replaceFirstChar { it.uppercase() }}, ${stringResource(R.string.detail_delicious)}"
        }
        
        Scaffold(
            topBar = {
                Header(title = categoryMessage, navController = navController)
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                
                // Quantity and Buy section
                item {
                    QuantityBuySection(
                        quantity = quantity,
                        price = product.price,
                        onIncrease = { 
                            if (quantity < 10) quantity++ 
                        },
                        onDecrease = { 
                            if (quantity > 1) quantity-- 
                        },
                        onBuyNow = {
                            if (viewModel.isUserLoggedIn()) {
                                repeat(quantity) {
                                    onAddToCart()
                                }
                                showAddedToCartMessage = true
                            } else {
                                onAddToCart() // This will trigger the navigation to login
                            }
                        }
                    )
                }
                
                // Sección de productos relacionados
                if (relatedProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.detail_related_products),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 16.dp)
                        )
                    }
                    
                    // Products grid usando componente reutilizable
                    ProductGrid(
                        products = relatedProducts,
                        isLoggedIn = viewModel.isUserLoggedIn(),
                        onProductClick = { relatedProduct ->
                            navController.navigate("detail/${relatedProduct.id}")
                        },
                        onAddToCart = { relatedProduct ->
                            if (viewModel.isUserLoggedIn()) {
                                onAddRelatedToCart(relatedProduct.id)
                            } else {
                                navController.navigate("login?returnTo=detail/${relatedProduct.id}")
                            }
                        },
                        horizontalPadding = 20
                    )
                }
            }
        }
        
        // Show success message when product is added
        if (showAddedToCartMessage) {
            LaunchedEffect(showAddedToCartMessage) {
                delay(2000) // Show for 2 seconds
                showAddedToCartMessage = false
            }
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 80.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = stringResource(R.string.detail_added_to_cart),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                    text = stringResource(R.string.detail_product_not_found),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.detail_product_not_exists),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}