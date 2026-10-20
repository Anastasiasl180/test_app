package com.example.testapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testapp.ui.game.GameUiEventsHandler
import com.example.testapp.ui.levels.LevelsUiEventsHandler
import com.example.testapp.ui.menu.MenuUiEventsHandler
import com.example.testapp.ui.profile.ProfileUiEventsHandler
import com.example.testapp.ui.rules.RulesUiEventsHandler
import com.example.testapp.ui.settings.SettingsUiEventsHandler
import com.example.testapp.ui.shop.ShopUiEventsHandler
import com.example.testapp.ui.webview.WebViewUiEventsHandler

@Composable
fun NavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = AppRoute.MenuScreen) {

        composable<AppRoute.MenuScreen> {
            MenuUiEventsHandler(navController = nav)
        }

        composable<AppRoute.LevelScreen> {
            LevelsUiEventsHandler(navController = nav)
        }

        composable<AppRoute.GameScreen> {
            GameUiEventsHandler(navController = nav)
        }

        composable<AppRoute.ShopScreen> {
            ShopUiEventsHandler(navController = nav)
        }

        composable<AppRoute.SettingsScreen> {
            SettingsUiEventsHandler(navController = nav)
        }

        composable<AppRoute.ProfileScreen> {
            ProfileUiEventsHandler(navController = nav)
        }

        composable<AppRoute.RulesScreen> {
            RulesUiEventsHandler(navController = nav)
        }

        composable<AppRoute.WebViewPart> {
            WebViewUiEventsHandler(navController = nav)
        }
    }
}
