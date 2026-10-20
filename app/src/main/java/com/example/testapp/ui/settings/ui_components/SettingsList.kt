package com.example.testapp.ui.settings.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText

private val LabelText = Color(0xFFF3E4D8)
private val PillBackground = Color(0xFF140807)
private val PillText = Color(0xFFE7A08F)

@Composable
fun SettingsList(
    musicEnabled: Boolean,
    vibrationEnabled: Boolean,
    soundEffectsEnabled: Boolean,
    notificationsEnabled: Boolean,
    onMusicChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    onSoundEffectsChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onLanguageClick: () -> Unit,
    onWebViewClick: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.89f)
            .fillMaxHeight(0.97f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsRow(icon = R.drawable.smth_r, title = stringResource(R.string.settings_music)) {
            ASwitch(checked = musicEnabled, onCheckedChange = onMusicChange)
        }
        SettingsRow(icon = R.drawable.smth_b, title = stringResource(R.string.settings_sound_effects)) {
            ASwitch(checked = soundEffectsEnabled, onCheckedChange = onSoundEffectsChange)
        }
        SettingsRow(icon = R.drawable.smth_g, title = stringResource(R.string.settings_vibration)) {
            ASwitch(checked = vibrationEnabled, onCheckedChange = onVibrationChange)
        }
        SettingsRow(icon = R.drawable.smth_y, title = stringResource(R.string.settings_notifications)) {
            ASwitch(checked = notificationsEnabled, onCheckedChange = onNotificationsChange)
        }
        SettingsRow(icon = R.drawable.smth_v, title = stringResource(R.string.settings_language)) {
            LanguagePill(onClick = onLanguageClick)
        }
        SettingsRow(
            icon = R.drawable.smth_p,
            title = stringResource(R.string.settings_restore_purchases),
            onClick = onWebViewClick
        ) {
            AsyncImage(
                model = R.drawable.polygon_brown,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .aspectRatio(27f / 32f)
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: Int,
    title: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
) {
    NButton(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        buttonImage = R.drawable.settings_field,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(976f / 156f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.04f))
            AsyncImage(
                model = icon,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.58f)
                    .aspectRatio(1f)
            )
            Box(modifier = Modifier.weight(0.04f))
            NText(
                text = title,
                color = LabelText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0f,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(0.3f)
            )
            Box(
                modifier = Modifier
                    .weight(0.26f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                trailing()
            }
            Box(modifier = Modifier.weight(0.03f))
        }
    }
}

@Composable
private fun ASwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    NButton(
        onClick = { onCheckedChange(!checked) },
        buttonImage = if (checked) R.drawable.on else R.drawable.off,
        modifier = Modifier
            .fillMaxHeight(if (checked) 0.5f else 0.41f)
            .aspectRatio(if (checked) 140f / 80f else 120f / 63f)
    )
}

@Composable
private fun LanguagePill(onClick: () -> Unit) {
    NButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight(0.46f)
            .aspectRatio(2f)
            .background(PillBackground, RoundedCornerShape(percent = 50))
    ) {
        NText(
            text = stringResource(R.string.language_code),
            color = PillText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.5f)
        )
    }
}
