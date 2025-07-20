package com.luciaaldana.eccomerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luciaaldana.eccomerceapp.feature.login.LoginScreen
import com.luciaaldana.eccomerceapp.feature.register.RegisterScreen
import com.luciaaldana.eccomerceapp.feature.home.ProductListScreen
import com.luciaaldana.eccomerceapp.feature.home.DetailScreen
import com.luciaaldana.eccomerceapp.feature.cart.CartScreen
import com.luciaaldana.eccomerceapp.feature.profile.ProfileScreen
import com.luciaaldana.eccomerceapp.feature.profile.OrderHistoryScreen
import com.luciaaldana.eccomerceapp.feature.profile.OrderConfirmationScreen
import com.luciaaldana.eccomerceapp.feature.cart.CartViewModel
import com.luciaaldana.eccomerceapp.feature.home.ProductsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController, 
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "productList", modifier = modifier) {
        composable( 
            route = "login?returnTo={returnTo}",
            arguments = listOf(navArgument("returnTo") { 
                type = NavType.StringType
                defaultValue = "productList"
                nullable = true
            })
        ) { backStackEntry ->
            val returnTo = backStackEntry.arguments?.getString("returnTo") ?: "productList"
            LoginScreen(navController, returnTo)
        }
        composable( route = "register") { RegisterScreen(navController) }
        composable( route = "productList") { ProductListScreen(navController, cartViewModel) }
        composable( route = "cart") { CartScreen(navController, cartViewModel) }
        composable( route = "profile") { 
            val viewModel: ProductsViewModel = hiltViewModel()
            if (viewModel.isUserLoggedIn()) {
                ProfileScreen(navController)
            } else {
                navController.navigate("login?returnTo=profile") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }
        composable( route = "orderHistory") { OrderHistoryScreen(navController) }
        composable( route = "orderConfirmation") { OrderConfirmationScreen(navController) }
        composable(
            route = "detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId != null) {
                val viewModel: ProductsViewModel = hiltViewModel()
                val allProducts = viewModel.allProducts.collectAsState().value
                val product = allProducts.find { it.id == productId }

                product?.let {
                    DetailScreen(
                        productId = productId,
                        navController = navController,
                        onAddToCart = { 
                            if (viewModel.isUserLoggedIn()) {
                                cartViewModel.add(it)
                            } else {
                                navController.navigate("login?returnTo=detail/$productId")
                            }
                        },
                        onAddRelatedToCart = { relatedProductId ->
                            allProducts.find { p -> p.id == relatedProductId }?.let { relatedProduct ->
                                cartViewModel.add(relatedProduct)
                            }
                        }
                    )
                }
            }
        }
    }
}
