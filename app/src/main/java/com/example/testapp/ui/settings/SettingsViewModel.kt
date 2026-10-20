package com.example.testapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.storage.AppStorage
import com.example.testapp.storage.Settings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class SettingsViewModel(private val repo: AppStorage) : ViewModel() {

    private val _currentPrefs = MutableStateFlow(repo.loadSettings())
    val currentPrefs: StateFlow<Settings> = _currentPrefs.asStateFlow()

    private val _balance = MutableStateFlow(repo.loadPlayer().balance)
    val balance: StateFlow<Int> = _balance.asStateFlow()

    private val _event = MutableSharedFlow<SettingsEffect>()
    val event = _event.asSharedFlow()

    fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SetMusic -> applyMusicToggle(intent.on)
            is SettingsIntent.SetVibration -> _currentPrefs.update { it.copy(vibrationEnabled = intent.on) }
            is SettingsIntent.SetSoundEffects -> _currentPrefs.update { it.copy(soundEffectsEnabled = intent.on) }
            is SettingsIntent.SetNotifications -> _currentPrefs.update { it.copy(notificationsEnabled = intent.on) }
            SettingsIntent.Confirm -> commitAndExit()
            SettingsIntent.OpenWebView -> openWebView()
        }
    }

    private fun applyMusicToggle(enabled: Boolean) {
        _currentPrefs.update { it.copy(musicEnabled = enabled) }
        emit(SettingsEffect.MusicToggled(enabled))
    }

    private fun openWebView() {
        repo.persistSettings(_currentPrefs.value)
        emit(SettingsEffect.OpenWebView)
    }

    private fun commitAndExit() {
        repo.persistSettings(_currentPrefs.value)
        emit(SettingsEffect.NavigateAway)
    }

    private fun emit(effect: SettingsEffect) {
        viewModelScope.launch { _event.emit(effect) }
    }
}

sealed interface SettingsIntent {
    data class SetMusic(val on: Boolean) : SettingsIntent
    data class SetVibration(val on: Boolean) : SettingsIntent
    data class SetSoundEffects(val on: Boolean) : SettingsIntent
    data class SetNotifications(val on: Boolean) : SettingsIntent
    data object Confirm : SettingsIntent
    data object OpenWebView : SettingsIntent
}

sealed interface SettingsEffect {
    data class MusicToggled(val on: Boolean) : SettingsEffect
    data object NavigateAway : SettingsEffect
    data object OpenWebView : SettingsEffect
}
