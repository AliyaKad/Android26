package ru.itis.library.remoteconfig.compose

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun ProvideRemoteConfigTheme(content: @Composable () -> Unit) {
    val primaryColor by rememberRemoteConfigColor("theme_primary", Color(0xFF6750A4))
    val backgroundColor by rememberRemoteConfigColor("theme_background", Color.White)
    val isDarkMode by rememberRemoteConfigBoolean("theme_dark_mode", false)

    val colorScheme: ColorScheme = if (isDarkMode) {
        darkColorScheme(primary = primaryColor, background = backgroundColor)
    } else {
        lightColorScheme(primary = primaryColor, background = backgroundColor)
    }

    MaterialTheme(colorScheme = colorScheme, content = content)
}