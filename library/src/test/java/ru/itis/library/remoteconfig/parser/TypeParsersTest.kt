package ru.itis.library.remoteconfig.parser

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TypeParsersTest {

    @Test
    fun `parseBoolean returns true for valid true strings`() {
        assertTrue(parseBoolean("true", false))
        assertTrue(parseBoolean("TRUE", false))
        assertTrue(parseBoolean("TrUe", false))
    }

    @Test
    fun `parseBoolean returns false for valid false strings`() {
        assertFalse(parseBoolean("false", true))
        assertFalse(parseBoolean("FALSE", true))
    }

    @Test
    fun `parseBoolean returns default for invalid strings`() {
        assertTrue(parseBoolean("yes", true))
        assertFalse(parseBoolean("1", false))
        assertTrue(parseBoolean("random", true))
    }

    @Test
    fun `parseBoolean returns default for empty string`() {
        assertTrue(parseBoolean("", true))
        assertFalse(parseBoolean("", false))
    }

    @Test
    fun `parseInt returns correct integer for valid string`() {
        assertEquals(42, parseInt("42", 0))
        assertEquals(-10, parseInt("-10", 0))
        assertEquals(0, parseInt("0", 99))
    }

    @Test
    fun `parseInt returns default for invalid string`() {
        assertEquals(99, parseInt("abc", 99))
        assertEquals(0, parseInt("12.34", 0))
        assertEquals(-1, parseInt("", -1))
    }

    @Test
    fun `parseDouble returns correct double for valid string`() {
        assertEquals(3.14, parseDouble("3.14", 0.0), 0.001)
        assertEquals(-2.5, parseDouble("-2.5", 0.0), 0.001)
    }

    @Test
    fun `parseDouble returns default for invalid string`() {
        assertEquals(1.0, parseDouble("not_a_number", 1.0), 0.001)
        assertEquals(0.0, parseDouble("12,34", 0.0), 0.001)
    }

    @Test
    fun `parseColorHex returns correct Color for valid HEX`() {
        val expected = Color(0xFFFF0000.toInt())
        assertEquals(expected, parseColorHex("#FF0000", Color.Blue))
        assertEquals(expected, parseColorHex("  #FF0000  ", Color.Blue))
    }

    @Test
    fun `parseColorHex returns correct Color for valid ARGB HEX`() {
        val expected = Color(0x80FF0000.toInt())
        assertEquals(expected, parseColorHex("#80FF0000", Color.Blue))
    }

    @Test
    fun `parseColorHex returns default for missing hash`() {
        assertEquals(Color.Green, parseColorHex("FF0000", Color.Green))
    }

    @Test
    fun `parseColorHex returns default for invalid HEX`() {
        assertEquals(Color.Black, parseColorHex("#GGGGGG", Color.Black))
        assertEquals(Color.White, parseColorHex("", Color.White))
        assertEquals(Color.Yellow, parseColorHex("#123", Color.Yellow))
    }
}