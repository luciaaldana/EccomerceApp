package com.luciaaldana.eccomerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luciaaldana.eccomerceapp.navigation.AppNavGraph
import com.luciaaldana.eccomerceapp.core.ui.components.BottomNavBar
import com.luciaaldana.eccomerceapp.core.ui.theme.EccomerceAppTheme
import com.luciaaldana.eccomerceapp.core.ui.theme.ThemeMode
import com.luciaaldana.eccomerceapp.feature.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            
            EccomerceAppTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val cartViewModel: CartViewModel = hiltViewModel()
                val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
                val cartItemCount = cartItems.sumOf { it.quantity }
                

                val showBottomBar = currentRoute in listOf("productList", "detail/{productId}", "profile")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(
                                navController = navController,
                                cartItemCount = cartItemCount
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        cartViewModel = cartViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EccomerceAppTheme(themeMode = ThemeMode.SYSTEM) {
        Greeting("Android")
    }
}