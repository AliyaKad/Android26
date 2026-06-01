package ru.itis.library.remoteconfig.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.itis.library.remoteconfig.core.RemoteConfigManager

@Composable
fun rememberRemoteConfigAutoRefresh(intervalMs: Long) {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }

    DisposableEffect(manager, intervalMs) {
        manager.enableAutoRefresh(intervalMs)
        onDispose { manager.disableAutoRefresh() }
    }
}

@Composable
fun rememberRemoteConfigRefresh(): () -> Unit {
    val context = LocalContext.current
    val manager = remember { RemoteConfigManager.getInstance(context) }
    return { manager.refreshConfig() }
}