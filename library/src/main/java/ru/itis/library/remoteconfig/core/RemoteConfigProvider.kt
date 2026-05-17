package ru.itis.library.remoteconfig.core

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

object RemoteConfigProvider {
    private var initialized = false

    fun initialize(context: Context) {
        if (!initialized) {
            FirebaseApp.initializeApp(context)
            val config = FirebaseRemoteConfig.getInstance()
            val settings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
            config.setConfigSettingsAsync(settings)
            initialized = true
        }
    }

    fun getString(key: String, default: String): String {
        return FirebaseRemoteConfig.getInstance().getString(key)
    }


    fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        val config = FirebaseRemoteConfig.getInstance()
        config.fetchAndActivate().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}