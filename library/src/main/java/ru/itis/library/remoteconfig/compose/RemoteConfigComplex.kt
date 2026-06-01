package ru.itis.library.remoteconfig.compose

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.KSerializer
import ru.itis.library.remoteconfig.core.RemoteConfigManager

@Composable
fun rememberRemoteConfigColor(key: String, default: Color): State<Color> {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }
    val defaultValue by rememberUpdatedState(default)
    val state = remember { mutableStateOf(defaultValue) }

    LaunchedEffect(manager) {
        manager.lastFetchTime
            .onEach { state.value = manager.getColor(key, defaultValue) }
            .launchIn(this)
    }
    return state
}

@Composable
fun <T> rememberRemoteConfigJson(key: String, default: T, serializer: KSerializer<T>): State<T> {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }
    val defaultValue by rememberUpdatedState(default)
    val state = remember { mutableStateOf(defaultValue) }

    LaunchedEffect(manager) {
        manager.lastFetchTime
            .onEach { state.value = manager.getJson(key, defaultValue, serializer) }
            .launchIn(this)
    }
    return state
}