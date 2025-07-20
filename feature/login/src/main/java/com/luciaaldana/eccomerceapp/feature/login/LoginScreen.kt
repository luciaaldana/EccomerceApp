package com.luciaaldana.eccomerceapp.feature.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.luciaaldana.eccomerceapp.core.ui.components.EmailTextField
import com.luciaaldana.eccomerceapp.core.ui.components.LoginHeader
import com.luciaaldana.eccomerceapp.core.ui.components.PasswordTextField
import com.luciaaldana.eccomerceapp.core.ui.components.PrimaryButton
import com.luciaaldana.eccomerceapp.core.ui.components.OutlinedButton

@Composable
fun LoginScreen(
    navController: NavController,
    returnTo: String = "productList",
    viewModel: LoginViewModel = hiltViewModel()
) {
    if (viewModel.isLoggedIn) {
        LaunchedEffect(Unit) {
            if (returnTo == "profile") {
                // Si va al perfil después del login, establecer productList como base
                navController.navigate("productList") {
                    popUpTo(0) { inclusive = true }
                }
                navController.navigate("profile") {
                    popUpTo("productList") { inclusive = false }
                }
            } else {
                navController.navigate(returnTo) {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // Header with logo and welcome text
        LoginHeader(
            title = stringResource(R.string.login_welcome_title),
            subtitle = stringResource(R.string.login_welcome_subtitle)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Login form
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Email field
                EmailTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    isError = viewModel.errorMessage?.contains("Email", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("Email", ignoreCase = true) == true) {
                        stringResource(R.string.login_email_error)
                    } else null
                )
                
                // Password field
                PasswordTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    placeholder = stringResource(R.string.login_password_placeholder),
                    isError = viewModel.errorMessage?.contains("contraseña", ignoreCase = true) == true,
                    errorMessage = if (viewModel.errorMessage?.contains("contraseña", ignoreCase = true) == true) {
                        stringResource(R.string.login_password_error)
                    } else null
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Login button
                PrimaryButton(
                    text = if (viewModel.isLoading) stringResource(R.string.login_button_loading) else stringResource(R.string.login_button_text),
                    onClick = { viewModel.onLoginClick() },
                    enabled = !viewModel.isLoading
                )
                
                // Cancel button
                OutlinedButton(
                    text = stringResource(R.string.login_button_cancel),
                    onClick = { 
                        navController.navigate(returnTo) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }
        
        // Error message (outside card for better visibility)
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Register link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.login_no_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = { navController.navigate("register") }
            ) {
                Text(
                    text = stringResource(R.string.login_register_link),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}