package com.example.testapp

import android.content.Context
import android.media.MediaPlayer
import com.example.testapp.storage.AppStorage
import org.koin.core.annotation.Single

@Single
class ClickSoundPlayer(context: Context, private val storage: AppStorage) {

    private val player: MediaPlayer? = MediaPlayer.create(context, R.raw.button_click_sound)

    fun playIfEnabled() {
        if (!storage.loadSettings().soundEffectsEnabled) return
        val mp = player ?: return
        mp.seekTo(0)
        mp.start()
    }
}
