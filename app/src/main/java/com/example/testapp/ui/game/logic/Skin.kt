package com.example.testapp.ui.game.logic

import androidx.annotation.DrawableRes
import com.example.testapp.R

enum class Skin(
    val title: String,
    @DrawableRes val card: Int,
    val price: Int,
    val bonusGem: GemType,
) {
    Painite("PAINITE", R.drawable.skin_card_painite, price = 0, bonusGem = GemType.Red),
    Gear("GEAR", R.drawable.skin_card_gear, price = 450, bonusGem = GemType.Blue),
    Gem("GEM", R.drawable.skin_card_gem, price = 600, bonusGem = GemType.Green),
    Cog("COG", R.drawable.skin_card_cog, price = 750, bonusGem = GemType.Yellow),
    Red("RED", R.drawable.skin_card_red, price = 900, bonusGem = GemType.Violet),
    Glow("GLOW", R.drawable.skin_card_glow, price = 1050, bonusGem = GemType.Pink),
}
