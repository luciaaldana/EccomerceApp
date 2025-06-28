package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController) {
    Column {
        Text("Detail")
        Button(onClick = { navController.navigate("cart") }) {
            Text("Ir al carrito")
        }
    }
}
