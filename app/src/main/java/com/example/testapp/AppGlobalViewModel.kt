package com.example.testapp

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import com.example.testapp.storage.AppStorage
import com.example.testapp.storage.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Single

@Single
fun provideStorage(): AppStorage = AppStorage()

@Single
fun providePlayer(context: Context): MediaPlayer {
    val mp = MediaPlayer.create(context, R.raw.game_sound) ?: MediaPlayer()
    mp.isLooping = true
    mp.setVolume(1f, 1f)
    return mp
}

@KoinViewModel
class AppGlobalViewModel(
    private val repo: AppStorage,
    private val audioPlayer: MediaPlayer,
) : ViewModel() {

    private val _savedPrefs = MutableStateFlow(Settings())
    val savedPrefs: StateFlow<Settings> = _savedPrefs.asStateFlow()

    private val _livePrefs = MutableStateFlow(Settings())
    val livePrefs: StateFlow<Settings> = _livePrefs.asStateFlow()

    init {
        syncFromDisk()
    }

    fun togglePlayback() {
        val isPlaying = audioPlayer.isPlaying
        if (isPlaying) audioPlayer.pause() else audioPlayer.start()
        _livePrefs.update { current -> current.copy(musicEnabled = !isPlaying) }
    }

    fun persistCurrentPrefs() {
        val live = _livePrefs.value
        val snapshot = repo.loadSettings().copy(musicEnabled = live.musicEnabled)
        _savedPrefs.value = snapshot
        repo.persistSettings(snapshot)
    }

    private var isMusicMuted = false

    fun muteMusic() {
        isMusicMuted = true
        pauseAudio()
    }

    fun unmuteMusic() {
        isMusicMuted = false
        resumeAudio()
    }

    fun resumeAudio() {
        val fromDisk = repo.loadSettings()
        _savedPrefs.value = fromDisk
        if (!isMusicMuted && _livePrefs.value.musicEnabled && !audioPlayer.isPlaying) audioPlayer.start()
    }

    fun pauseAudio() {
        if (audioPlayer.isPlaying) audioPlayer.pause()
    }

    private fun syncFromDisk() {
        val loaded = repo.loadSettings()
        _savedPrefs.value = loaded
        _livePrefs.value = loaded
    }
}
