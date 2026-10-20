package com.example.testapp.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.settings.ui_components.SettingsHeader
import com.example.testapp.ui.settings.ui_components.SettingsList


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNotificationsChange: (Boolean) -> Unit,
    onLanguageClick: () -> Unit,
    onWebViewClick: (() -> Unit)? = null,
) {
    val prefs by viewModel.currentPrefs.collectAsState()
    val balance by viewModel.balance.collectAsState()

    NContainer(
        action = { viewModel.handleIntent(SettingsIntent.Confirm) },
        leftImage = R.drawable.icon_back,
        balance = balance,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    SettingsHeader()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                    SettingsList(
                        musicEnabled = prefs.musicEnabled,
                        vibrationEnabled = prefs.vibrationEnabled,
                        soundEffectsEnabled = prefs.soundEffectsEnabled,
                        notificationsEnabled = prefs.notificationsEnabled,
                        onMusicChange = { viewModel.handleIntent(SettingsIntent.SetMusic(it)) },
                        onVibrationChange = { viewModel.handleIntent(SettingsIntent.SetVibration(it)) },
                        onSoundEffectsChange = { viewModel.handleIntent(SettingsIntent.SetSoundEffects(it)) },
                        onNotificationsChange = onNotificationsChange,
                        onLanguageClick = onLanguageClick,
                        onWebViewClick = onWebViewClick
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
            }
        }
    )
}
