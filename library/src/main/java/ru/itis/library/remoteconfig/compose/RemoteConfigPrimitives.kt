package ru.itis.library.remoteconfig.compose

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.itis.library.remoteconfig.core.RemoteConfigManager

@Composable
fun rememberRemoteConfigBoolean(key: String, default: Boolean = false): State<Boolean> =
    rememberRemoteConfigValue(key, default) { manager, key, def -> manager.getBoolean(key, def) }

@Composable
fun rememberRemoteConfigString(key: String, default: String = ""): State<String> =
    rememberRemoteConfigValue(key, default) { manager, key, def -> manager.getString(key, def) }

@Composable
fun rememberRemoteConfigInt(key: String, default: Int = 0): State<Int> =
    rememberRemoteConfigValue(key, default) { manager, key, def -> manager.getInt(key, def) }

@Composable
fun rememberRemoteConfigDouble(key: String, default: Double = 0.0): State<Double> =
    rememberRemoteConfigValue(key, default) { manager, key, def -> manager.getDouble(key, def) }


@Composable
private fun <T> rememberRemoteConfigValue(
    key: String,
    default: T,
    retriever: (RemoteConfigManager, String, T) -> T
): State<T> {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }
    val defaultValue by rememberUpdatedState(default)
    val state = remember { mutableStateOf(defaultValue) }

    LaunchedEffect(manager) {
        manager.lastFetchTime
            .onEach { state.value = retriever(manager, key, defaultValue) }
            .launchIn(this)
    }
    return state
}