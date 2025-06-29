package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.viewmodel.LoginViewModel

@Composable
fun ProductListScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    Column {
        Text("ProductList")
        Button(onClick = { navController.navigate("detail") }) {
            Text("Ir al detalle")
        }

        Button(
            onClick = {
                loginViewModel.logout()
                navController.navigate("login") {
                    popUpTo("productList") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}
