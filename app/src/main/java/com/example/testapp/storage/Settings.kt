package com.example.testapp.storage

data class Settings(
    val musicEnabled: Boolean = false,
    val vibrationEnabled: Boolean = false,
    val soundEffectsEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
)
