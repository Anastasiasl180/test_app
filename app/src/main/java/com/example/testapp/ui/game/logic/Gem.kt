package com.example.testapp.ui.game.logic

import androidx.annotation.DrawableRes
import com.example.testapp.R
import kotlin.math.abs

enum class GemType(@DrawableRes val image: Int) {
    Red(R.drawable.smth_r),
    Blue(R.drawable.smth_b),
    Green(R.drawable.smth_g),
    Yellow(R.drawable.smth_y),
    Violet(R.drawable.smth_v),
    Pink(R.drawable.smth_p),
}

data class Gem(
    val id: Long,
    val type: GemType,
    val spawnRow: Int,
)

data class Cell(val row: Int, val col: Int) {
    fun isNeighbourOf(other: Cell): Boolean =
        abs(row - other.row) + abs(col - other.col) == 1
}

data class PlacedGem(val gem: Gem, val cell: Cell)
