package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun CartScreen(navController: NavController) {
    Column {
        Text("Cart")
        Button(onClick = { navController.navigate("orderHistory") }) {
            Text("Ir al historial")
        }
    }
}
