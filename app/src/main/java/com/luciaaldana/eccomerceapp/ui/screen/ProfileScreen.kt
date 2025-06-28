package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    Column {
        Text("OrderHistory")
        Button(onClick = { navController.navigate("login") }) {
            Text("Ir al login")
        }
    }
}
