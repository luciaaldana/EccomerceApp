package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun OrderHistoryScreen(navController: NavController) {
    Column {
        Text("OrderHistory")
        Button(onClick = { navController.navigate("profile") }) {
            Text("Ir al perfil")
        }
    }
}
