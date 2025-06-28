package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    Column {
        Text("Login")
        Button(onClick = { navController.navigate("productList") }) {
            Text("Ir a catálogo")
        }
    }
}
