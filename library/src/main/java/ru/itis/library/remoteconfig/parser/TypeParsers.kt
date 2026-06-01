package ru.itis.library.remoteconfig.parser

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme


fun parseBoolean(value: String, default: Boolean): Boolean {
    return when (value.trim().lowercase()) {
        "true" -> true
        "false" -> false
        else -> default
    }
}


fun parseInt(value: String, default: Int): Int {
    return try {
        value.toInt()
    } catch (e: NumberFormatException) {
        default
    }
}


fun parseDouble(value: String, default: Double): Double {
    return try {
        value.toDouble()
    } catch (e: NumberFormatException) {
        default
    }
}


fun parseColorHex(value: String, default: Color): Color {
    return try {
        val cleanValue = value.trim()
        if (cleanValue.isEmpty() || !cleanValue.startsWith("#")) {
            return default
        }
        val androidColor = parseColor(cleanValue)
        Color(androidColor)
    } catch (e: Exception) {
        default
    }
}


fun parseColorScheme(
    primaryHex: String?,
    backgroundHex: String?,
    isDark: Boolean,
    default: ColorScheme
): ColorScheme {
    return try {
        val primary = primaryHex?.let { parseColorHex(it, default.primary) } ?: default.primary
        val background = backgroundHex?.let { parseColorHex(it, default.background) } ?: default.background
        if (isDark) {
            darkColorScheme(primary = primary, background = background)
        } else {
            lightColorScheme(primary = primary, background = background)
        }
    } catch (e: Exception) {
        default
    }
}