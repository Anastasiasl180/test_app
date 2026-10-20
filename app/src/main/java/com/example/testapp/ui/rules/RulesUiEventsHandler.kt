package com.example.testapp.ui.rules

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.testapp.navigation.AppRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun RulesUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<RulesViewModel>()

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                RulesUiEvent.NavigateBack -> {
                    delay(150)
                    navController.navigate(AppRoute.LevelScreen) {
                        popUpTo<AppRoute.MenuScreen>()
                    }
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(RulesEvent.NavigateBack)
    }

    RulesScreen(viewModel = viewModel)
}
