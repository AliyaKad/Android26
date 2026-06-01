package ru.itis.library.remoteconfig.compose

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.itis.library.remoteconfig.core.RemoteConfigManager

@Composable
fun rememberExperimentGroup(experimentKey: String, default: String = "control"): State<String> {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }
    val defaultValue by rememberUpdatedState(default)
    val state = remember { mutableStateOf(defaultValue) }

    LaunchedEffect(manager) {
        manager.lastFetchTime
            .onEach { state.value = manager.getExperimentGroup(experimentKey, defaultValue) }
            .launchIn(this)
    }
    return state
}

@Composable
fun WhenRemoteConfig(
    condition: Boolean,
    contentIfTrue: @Composable () -> Unit,
    contentIfFalse: @Composable () -> Unit = {}
) {
    if (condition) contentIfTrue() else contentIfFalse()
}