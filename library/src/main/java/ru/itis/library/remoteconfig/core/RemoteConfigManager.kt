package ru.itis.library.remoteconfig.core

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import ru.itis.library.remoteconfig.parser.*

class RemoteConfigManager private constructor(context: Context) {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val _lastFetchTime = MutableStateFlow(System.currentTimeMillis())
    val lastFetchTime: StateFlow<Long> = _lastFetchTime.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var autoRefreshJob: Job? = null

    init {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(settings)
        refreshConfig()
    }

    fun refreshConfig() {
        scope.launch {
            try {
                remoteConfig.fetchAndActivate().await()
                _lastFetchTime.value = System.currentTimeMillis()
            } catch (e: Exception) {
            }
        }
    }

    fun getString(key: String, default: String): String =
        remoteConfig.getString(key).takeIf { it.isNotEmpty() } ?: default

    fun getBoolean(key: String, default: Boolean): Boolean =
        parseBoolean(remoteConfig.getString(key), default)

    fun getInt(key: String, default: Int): Int =
        parseInt(remoteConfig.getString(key), default)

    fun getDouble(key: String, default: Double): Double =
        parseDouble(remoteConfig.getString(key), default)

    fun getColor(key: String, default: Color): Color =
        parseColorHex(remoteConfig.getString(key), default)

    fun <T> getJson(key: String, default: T, serializer: KSerializer<T>): T {
        val raw = remoteConfig.getString(key)
        return try {
            if (raw.isEmpty()) default else json.decodeFromString(serializer, raw)
        } catch (e: Exception) {
            default
        }
    }

    fun getExperimentGroup(experimentKey: String, default: String = "control"): String =
        remoteConfig.getString(experimentKey).takeIf { it.isNotEmpty() } ?: default

    fun enableAutoRefresh(intervalMs: Long) {
        disableAutoRefresh()
        if (intervalMs > 0) {
            autoRefreshJob = scope.launch {
                while (true) {
                    delay(intervalMs)
                    refreshConfig()
                }
            }
        }
    }

    fun disableAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
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