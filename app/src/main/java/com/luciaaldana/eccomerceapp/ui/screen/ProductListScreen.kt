package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.luciaaldana.eccomerceapp.model.data.Product
import com.luciaaldana.eccomerceapp.viewmodel.ProductsViewModel

@Composable
fun ProductListScreen(navController: NavController) {
    val viewModel: ProductsViewModel = hiltViewModel()
    val products by viewModel.filteredProducts.collectAsState()
    val all by viewModel.allProducts.collectAsState()

    val searchText by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categories = remember (all) {
        all.map {it.category}.distinct()
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

        // Grid escalonado
        @OptIn(ExperimentalFoundationApi::class)
        (LazyVerticalStaggeredGrid (
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.Start,
    ) {
        items(products) { product ->
            ProductCard(product = product)
        }
    })
//        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            items(products) { product ->
//                ProductCard(product = product)
//            }
//        }

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
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = product.description)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "\$${product.price}")
            if (product.includesDrink) {
                Text(text = "Incluye bebida 🥤", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
