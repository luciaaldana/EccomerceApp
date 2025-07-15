package com.luciaaldana.eccomerceapp.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.luciaaldana.eccomerceapp.core.ui.theme.ThemeMode

data class ThemeOption(
    val mode: ThemeMode,
    val label: String,
    val icon: ImageVector
)

@Composable
fun ThemeToggle(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val themeOptions = listOf(
        ThemeOption(ThemeMode.LIGHT, "Claro", Icons.Default.LightMode),
        ThemeOption(ThemeMode.DARK, "Oscuro", Icons.Default.DarkMode),
        ThemeOption(ThemeMode.SYSTEM, "Sistema", Icons.Default.SettingsBrightness)
    )

    Row(
        modifier = modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        themeOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedTheme == option.mode),
                        onClick = { onThemeSelected(option.mode) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedTheme == option.mode),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (option != themeOptions.last()) {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}