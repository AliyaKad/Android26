package ru.itis.android26

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import ru.itis.android26.model.UiVariant
import ru.itis.library.remoteconfig.compose.*

@Serializable
data class PromoBanner(
    val title: String = "Акция!",
    val subtitle: String = "Только сегодня",
    val colorHex: String = "#FF5722"
)

@Composable
fun DemoApp() {

    val isFeatureEnabled by rememberRemoteConfigBoolean("feature_enabled", true)
    val buttonText by rememberRemoteConfigString("button_text", "Нажми меня")
    val bgColor by rememberRemoteConfigColor("bg_color", MaterialTheme.colorScheme.background)

    val banner by rememberRemoteConfigJson(
        key = "promo_banner",
        default = PromoBanner(),
        serializer = PromoBanner.serializer()
    )

    val rawVariant by rememberExperimentGroup("ui_variant", "control")
    val uiVariant = remember(rawVariant) { UiVariant.fromString(rawVariant) }


    rememberRemoteConfigAutoRefresh(intervalMs = 300_000)
    val refreshConfig = rememberRemoteConfigRefresh()

    Surface(modifier = Modifier.fillMaxSize(), color = bgColor) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = refreshConfig) {
                    Icon(Icons.Default.Refresh, contentDescription = "Обновить конфигурацию")
                }
            }

            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(banner.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(banner.subtitle, fontSize = 14.sp)
                }
            }

            when (uiVariant) {
                UiVariant.CONTROL -> {
                    Button(onClick = {}, enabled = isFeatureEnabled) { Text(buttonText) }
                }
                UiVariant.ROUNDED -> {
                    OutlinedButton(
                        onClick = {}, shape = RoundedCornerShape(24.dp), enabled = isFeatureEnabled
                    ) {
                        Text(buttonText, color = MaterialTheme.colorScheme.primary)
                    }
                }
                UiVariant.ICON -> {
                    Button(onClick = {}, enabled = isFeatureEnabled) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(buttonText)
                    }
                }
            }

            WhenRemoteConfig(
                condition = isFeatureEnabled,
                contentIfTrue = { Text("Функция включена", color = Color(0xFF4CAF50), fontSize = 14.sp) },
                contentIfFalse = { Text("ункция отключена", color = Color(0xFFF44336), fontSize = 14.sp) }
            )

            Text(
                "Текущий A/B вариант: ${uiVariant.name.lowercase()}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}