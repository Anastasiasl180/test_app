package com.example.testapp.storage

import com.example.testapp.ui.game.logic.Skin

data class Player(
    val nickname: String? = null,
    val photoUri: String? = null,
    val balance: Int = 0,
    val ownedSkins: Set<Skin> = setOf(Skin.Painite),
    val equippedSkin: Skin = Skin.Painite,
    val lastDailyReward: Long = 0L,
)
