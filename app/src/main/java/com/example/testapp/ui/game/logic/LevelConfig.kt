package com.example.testapp.ui.game.logic

data class LevelConfig(
    val level: Int,
    val moves: Int,
    val goalType: GemType,
    val goalCount: Int,
) {
    companion object {
        const val LEVELS_COUNT = 120
        const val MARATHON_LEVELS = LEVELS_COUNT / 2
        const val MOVES = 22

        fun forLevel(level: Int): LevelConfig {
            val types = GemType.entries
            return LevelConfig(
                level = level,
                moves = MOVES,
                goalType = types[(level - 1) % types.size],
                goalCount = 8 + (level - 1) / 4,
            )
        }

        fun starsFor(collected: Int, goal: Int): Int = when {
            collected >= goal -> 3
            collected * 2 >= goal -> 2
            collected > 0 -> 1
            else -> 0
        }
    }
}
