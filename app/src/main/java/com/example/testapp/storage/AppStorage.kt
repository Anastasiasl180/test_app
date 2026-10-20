package com.example.testapp.storage

import io.paperdb.Paper

class AppStorage() {

    fun loadSettings(): Settings =
        Paper.book().read(SETTINGS_KEY, Settings()) ?: Settings()

    fun persistSettings(value: Settings) {
        Paper.book().write(SETTINGS_KEY, value)
    }

    fun loadPlayer(): Player =
        Paper.book().read(PLAYER_KEY, Player()) ?: Player()

    fun persistPlayer(value: Player) {
        Paper.book().write(PLAYER_KEY, value)
    }

    fun loadProgress(): GameProgress =
        Paper.book().read(PROGRESS_KEY, GameProgress()) ?: GameProgress()

    fun persistProgress(value: GameProgress) {
        Paper.book().write(PROGRESS_KEY, value)
    }

    companion object {
        private const val SETTINGS_KEY = "settings"
        private const val PLAYER_KEY = "player"
        private const val PROGRESS_KEY = "progress"
    }
}
