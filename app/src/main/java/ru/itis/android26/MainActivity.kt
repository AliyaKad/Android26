package ru.itis.android26

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.itis.library.remoteconfig.compose.ProvideRemoteConfigTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideRemoteConfigTheme {
                DemoApp()
            }
        }
    }
}