package com.luciaaldana.eccomerceapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    Column {
        Text("Register")
        Button(onClick = { navController.navigate("login") }) {
            Text("Ir al login")
        }
    }
}
