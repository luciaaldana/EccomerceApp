package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProductListScreen(navController: NavController) {
    Column {
        Text("ProductList")
        Button(onClick = { navController.navigate("detail") }) {
            Text("Ir al detalle")
        }
    }
}
