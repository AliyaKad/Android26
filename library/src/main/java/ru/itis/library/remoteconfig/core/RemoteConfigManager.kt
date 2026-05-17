package ru.itis.library.remoteconfig.core

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import ru.itis.library.remoteconfig.parser.parseBoolean
import ru.itis.library.remoteconfig.parser.parseDouble
import ru.itis.library.remoteconfig.parser.parseInt
import ru.itis.library.remoteconfig.parser.parseColorHex

class RemoteConfigManager private constructor(context: Context) {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val json = Json { ignoreUnknownKeys = true }

    init {
        FirebaseApp.initializeApp(context)
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.fetchAndActivate()
    }

    fun getString(key: String, default: String): String {
        return remoteConfig.getString(key).takeIf { it.isNotEmpty() } ?: default
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        val raw = remoteConfig.getString(key)
        return parseBoolean(raw, default)
    }

    fun getInt(key: String, default: Int): Int {
        val raw = remoteConfig.getString(key)
        return parseInt(raw, default)
    }

    fun getDouble(key: String, default: Double): Double {
        val raw = remoteConfig.getString(key)
        return parseDouble(raw, default)
    }

    fun getColor(key: String, default: Color): Color {
        val raw = remoteConfig.getString(key)
        return parseColorHex(raw, default)
    }


    fun <T> getJson(key: String, default: T, serializer: KSerializer<T>): T {
        val raw = remoteConfig.getString(key)
        return try {
            if (raw.isEmpty()) default else json.decodeFromString(serializer, raw)
        } catch (e: Exception) {
            default
        }
    }

    fun getExperimentGroup(experimentKey: String, default: String = "control"): String {
        return remoteConfig.getString(experimentKey).takeIf { it.isNotEmpty() } ?: default
    }


    companion object {
        @Volatile
        private var INSTANCE: RemoteConfigManager? = null

        fun getInstance(context: Context): RemoteConfigManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RemoteConfigManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}