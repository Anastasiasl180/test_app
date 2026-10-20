package com.example.testapp.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object MenuScreen : AppRoute

    @Serializable
    data object LevelScreen : AppRoute

    @Serializable
    data object ProfileScreen : AppRoute

    @Serializable
    data object SettingsScreen : AppRoute

    @Serializable
    data object ShopScreen : AppRoute

    @Serializable
    data object GameScreen : AppRoute

    @Serializable
    data object RulesScreen : AppRoute

    @Serializable
    data object WebViewPart : AppRoute

}
