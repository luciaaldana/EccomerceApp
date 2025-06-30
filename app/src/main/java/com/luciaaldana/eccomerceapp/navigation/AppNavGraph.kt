package com.luciaaldana.eccomerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luciaaldana.eccomerceapp.ui.screen.*
import com.luciaaldana.eccomerceapp.viewmodel.CartViewModel
import com.luciaaldana.eccomerceapp.viewmodel.ProductsViewModel

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable( route = "login") { LoginScreen(navController) }
        composable( route = "register") { RegisterScreen(navController) }
        composable( route = "productList") { ProductListScreen(navController) }
        composable( route = "cart") { CartScreen(navController) }
        composable( route = "profile") { ProfileScreen(navController) }
        composable( route = "orderHistory") { OrderHistoryScreen(navController) }
        composable( route = "orderConfirmation") { OrderConfirmationScreen(navController) }
        composable("detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                val cartViewModel: CartViewModel = hiltViewModel()
                val viewModel: ProductsViewModel = hiltViewModel()
                val allProducts = viewModel.allProducts.collectAsState().value
                val product = allProducts.find { it.id == productId }

                if (product != null) {
                    DetailScreen(
                        productId = productId,
                        navController = navController,
                        onAddToCart = { cartViewModel.add(product) }
                    )
                }
            }
        }
    }
}
