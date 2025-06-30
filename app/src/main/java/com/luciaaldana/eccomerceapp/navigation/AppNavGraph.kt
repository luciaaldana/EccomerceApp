package com.luciaaldana.eccomerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luciaaldana.eccomerceapp.ui.screen.*

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable( route = "login") { LoginScreen(navController) }
        composable( route = "register") { RegisterScreen(navController) }
        composable( route = "productList") { ProductListScreen(navController) }
        composable( route = "cart") { CartScreen(navController) }
        composable( route = "detail") { DetailScreen(navController) }
        composable( route = "profile") { ProfileScreen(navController) }
        composable( route = "orderHistory") { OrderHistoryScreen(navController) }
        composable( route = "orderConfirmation") { OrderConfirmationScreen(navController) }
    }
}
