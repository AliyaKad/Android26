package ru.itis.library.remoteconfig.parser

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class TypeParsersTest {

    @Test
    fun parseBoolean_validTrue_returnsTrue() {
        assertEquals(true, parseBoolean("true", false))
        assertEquals(true, parseBoolean("TRUE", false))
    }

    @Test
    fun parseBoolean_validFalse_returnsFalse() {
        assertEquals(false, parseBoolean("false", true))
        assertEquals(false, parseBoolean("anything", true))
    }

    @Test
    fun parseBoolean_invalid_returnsDefault() {
        assertEquals(false, parseBoolean("", false))
        assertEquals(true, parseBoolean("not_a_bool", true))
    }

    @Test
    fun parseInt_validNumber_returnsInt() {
        assertEquals(42, parseInt("42", 0))
    }

    @Test
    fun parseInt_invalid_returnsDefault() {
        assertEquals(-1, parseInt("abc", -1))
        assertEquals(0, parseInt("", 0))
    }

    @Test
    fun parseDouble_validNumber_returnsDouble() {
        assertEquals(3.14, parseDouble("3.14", 0.0), 0.001)
    }

    @Test
    fun parseColorHex_validHex_returnsColor() {
        val result = parseColorHex("#FF0000", Color.Unspecified)
        assertEquals(Color.Red, result)
    }

    @Test
    fun parseColorHex_invalidHex_returnsDefault() {
        val default = Color.Blue
        assertEquals(default, parseColorHex("#ZZ0000", default))
        assertEquals(default, parseColorHex("", default))
        assertEquals(default, parseColorHex("red", default))
    }
}