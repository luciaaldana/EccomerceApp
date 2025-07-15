package com.luciaaldana.eccomerceapp.core.ui.theme

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(
        "theme_preferences", 
        Context.MODE_PRIVATE
    )
    
    private val _themeMode = MutableStateFlow(getCurrentThemeMode())
    val themeMode: Flow<ThemeMode> = _themeMode.asStateFlow()

    private fun getCurrentThemeMode(): ThemeMode {
        val savedTheme = sharedPrefs.getString("theme_mode", "SYSTEM")
        return when (savedTheme) {
            "LIGHT" -> ThemeMode.LIGHT
            "DARK" -> ThemeMode.DARK
            "SYSTEM" -> ThemeMode.SYSTEM
            else -> ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(themeMode: ThemeMode) {
        sharedPrefs.edit()
            .putString("theme_mode", themeMode.name)
            .apply()
        _themeMode.value = themeMode
    }
}