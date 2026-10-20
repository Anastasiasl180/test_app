package com.example.testapp.ui.levels

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.testapp.navigation.AppRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun LevelsUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<LevelsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            delay(150)
            when (event) {
                LevelsUiEvent.NavigateBack -> navController.popBackStack(AppRoute.MenuScreen, inclusive = false)

                LevelsUiEvent.NavigateToGame -> navController.navigate(AppRoute.GameScreen) {
                    popUpTo<AppRoute.MenuScreen>()
                }

                LevelsUiEvent.NavigateToRules -> navController.navigate(AppRoute.RulesScreen) {
                    popUpTo<AppRoute.MenuScreen>()
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(LevelsEvent.NavigateBack)
    }

    LevelsScreen(viewModel = viewModel)
}
