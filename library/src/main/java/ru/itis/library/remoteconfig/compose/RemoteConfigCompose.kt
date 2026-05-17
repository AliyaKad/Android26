package ru.itis.library.remoteconfig.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.serialization.KSerializer
import ru.itis.library.remoteconfig.core.RemoteConfigManager

@Composable
fun rememberRemoteConfigBoolean(
    key: String,
    default: Boolean = false
): State<Boolean> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getBoolean(key, default)
    }

    return state
}

@Composable
fun rememberRemoteConfigString(
    key: String,
    default: String = ""
): State<String> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getString(key, default)
    }

    return state
}

@Composable
fun rememberRemoteConfigInt(
    key: String,
    default: Int = 0
): State<Int> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getInt(key, default)
    }

    return state
}

@Composable
fun rememberRemoteConfigDouble(
    key: String,
    default: Double = 0.0
): State<Double> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getDouble(key, default)
    }

    return state
}

@Composable
fun rememberRemoteConfigColor(
    key: String,
    default: Color
): State<Color> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getColor(key, default)
    }

    return state
}

@Composable
fun <T> rememberRemoteConfigJson(
    key: String,
    default: T,
    serializer: KSerializer<T>
): State<T> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getJson(key, default, serializer)
    }

    return state
}

@Composable
fun WhenRemoteConfig(
    condition: Boolean,
    contentIfTrue: @Composable () -> Unit,
    contentIfFalse: @Composable () -> Unit = {}
) {
    if (condition) {
        contentIfTrue()
    } else {
        contentIfFalse()
    }
}

@Composable
fun rememberExperimentGroup(
    experimentKey: String,
    default: String = "control"
): State<String> {
    val state = remember { mutableStateOf(default) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val manager = RemoteConfigManager.getInstance(context)
        state.value = manager.getExperimentGroup(experimentKey, default)
    }

    return state
}