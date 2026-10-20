package com.example.testapp.storage

import com.example.testapp.ui.game.logic.Achievement

data class GameProgress(
    val completedLevels: Set<Int> = emptySet(),
    val levelStars: Map<Int, Int> = emptyMap(),
    val bestScores: Map<Int, Int> = emptyMap(),
    val selectedLevel: Int = 1,
    val achievements: Set<Achievement> = emptySet(),
)
