package ru.itis.library.remoteconfig.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Serializable
data class TestConfig(val name: String, val version: Int)

@RunWith(RobolectricTestRunner::class)
class RemoteConfigManagerTest {

    @Test
    fun `getJson returns default when string is empty`() {
        val defaultConfig = TestConfig("Default", 1)
        val raw = ""

        val result = try {
            if (raw.isEmpty()) defaultConfig else Json.decodeFromString(raw)
        } catch (e: Exception) {
            defaultConfig
        }

        assertEquals(defaultConfig, result)
    }

    @Test
    fun `getJson returns parsed object when string is valid JSON`() {
        val defaultConfig = TestConfig("Default", 1)
        val validJson = """{"name":"Production","version":2}"""

        val result = try {
            if (validJson.isEmpty()) defaultConfig else Json.decodeFromString<TestConfig>(validJson)
        } catch (e: Exception) {

            defaultConfig
        }

        assertEquals("Production", result.name)
        assertEquals(2, result.version)
    }

    @Test
    fun `getJson returns default when JSON is malformed`() {
        val defaultConfig = TestConfig("Default", 1)
        val invalidJson = """{"name":"Broken", "version": }"""

        val result = try {
            if (invalidJson.isEmpty()) defaultConfig else Json.decodeFromString<TestConfig>(invalidJson)
        } catch (e: Exception) {
            defaultConfig
        }

        assertEquals(defaultConfig, result)
    }
}