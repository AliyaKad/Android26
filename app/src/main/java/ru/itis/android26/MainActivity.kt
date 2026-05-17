package ru.itis.android26

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.itis.library.remoteconfig.compose.rememberRemoteConfigBoolean
import ru.itis.library.remoteconfig.compose.rememberRemoteConfigColor
import ru.itis.library.remoteconfig.compose.rememberRemoteConfigString
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import ru.itis.library.remoteconfig.compose.WhenRemoteConfig
import ru.itis.library.remoteconfig.compose.rememberExperimentGroup
import ru.itis.library.remoteconfig.compose.rememberRemoteConfigJson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}


@Serializable
data class PromoBanner(
    val title: String = "Акция!",
    val subtitle: String = "Только сегодня",
    val colorHex: String = "#FF5722"
)

@Composable
fun MyApp() {
    val isEnabled by rememberRemoteConfigBoolean("feature_enabled", true)
    val buttonText by rememberRemoteConfigString("button_text", "Кнопка")
    val bgColor by rememberRemoteConfigColor("bg_color", Color.White)


    val banner by rememberRemoteConfigJson(
        key = "promo_banner",
        default = PromoBanner(),
        serializer = PromoBanner.serializer()
    )

    val uiVariant by rememberExperimentGroup("ui_variant", "control")


    Log.d("AB_TEST", "=== UI Variant: $uiVariant ===")



    Surface(color = bgColor) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(banner.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(banner.subtitle, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            when (uiVariant) {
                "control" -> {
                    Button(onClick = {}, enabled = isEnabled) {
                        Text(buttonText)
                    }
                }
                "rounded" -> {
                    OutlinedButton(
                        onClick = {},
                        shape = RoundedCornerShape(24.dp),
                        enabled = isEnabled
                    ) {
                        Text(buttonText, color = Color.Blue)
                    }
                }
                "icon" -> {
                    Button(onClick = {}, enabled = isEnabled) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(buttonText)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            WhenRemoteConfig(
                condition = remember { isEnabled },
                contentIfTrue = {
                    Text("Функция включена", color = Color.Green)
                },
                contentIfFalse = {
                    Text("Функция отключена", color = Color.Red)
                }
            )
        }
    }
}