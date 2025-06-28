package com.luciaaldana.eccomerceapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.luciaaldana.eccomerceapp.ui.screen.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable( route = "login") { LoginScreen(navController) }
        composable( route = "register") { RegisterScreen(navController) }
        composable( route = "productList") { ProductListScreen(navController) }
        composable( route = "cart") { CartScreen(navController) }
        composable( route = "detail") { DetailScreen(navController) }
        composable( route = "profile") { ProfileScreen(navController) }
        composable( route = "orderHistory") { OrderHistoryScreen(navController) }
    }
}
