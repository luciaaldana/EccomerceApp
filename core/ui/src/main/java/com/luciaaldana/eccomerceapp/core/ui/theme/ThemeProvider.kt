package com.luciaaldana.eccomerceapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

data class ThemeState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isDynamicColorEnabled: Boolean = true
)

val LocalThemeState = compositionLocalOf { ThemeState() }

@Composable
fun ThemeProvider(
    initialThemeState: ThemeState = ThemeState(),
    content: @Composable () -> Unit
) {
    var themeState by remember { mutableStateOf(initialThemeState) }
    
    CompositionLocalProvider(
        LocalThemeState provides themeState,
        content = content
    )
}

@Composable
fun updateThemeMode(themeMode: ThemeMode) {
    val currentState = LocalThemeState.current
    // Note: This is a simplified approach. In a real app, you'd want to use a ViewModel or StateHolder
    // to manage the theme state properly
}