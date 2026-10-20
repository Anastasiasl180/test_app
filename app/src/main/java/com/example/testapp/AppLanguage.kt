package com.example.testapp

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object AppLanguage {
    const val ENGLISH = "en"
    const val UKRAINIAN = "uk"

    fun setDefaultIfNotChosen() {
        if (AppCompatDelegate.getApplicationLocales().isEmpty) set(ENGLISH)
    }

    fun set(language: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
}
