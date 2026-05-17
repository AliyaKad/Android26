package ru.itis.library.remoteconfig.parser

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color


fun parseBoolean(value: String, default: Boolean): Boolean {
    return try {
        value.equals("true", ignoreCase = true)
    } catch (e: Exception) {
        default
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