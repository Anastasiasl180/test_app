package com.example.testapp.ui.game.logic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.testapp.R

enum class Achievement(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val icon: Int,
    val descriptionArgs: List<Any> = emptyList(),
) {
    FirstMatch(R.string.achievement_first_match, R.string.achievement_first_match_text, R.drawable.smth_r),

    ComboKing(R.string.achievement_combo_king, R.string.achievement_combo_king_text, R.drawable.smth_b),

    Flawless(R.string.achievement_flawless, R.string.achievement_flawless_text, R.drawable.smth_g),

    Marathon(
        R.string.achievement_marathon,
        R.string.achievement_marathon_text,
        R.drawable.smth_y,
        descriptionArgs = listOf(LevelConfig.MARATHON_LEVELS)
    ),
}
