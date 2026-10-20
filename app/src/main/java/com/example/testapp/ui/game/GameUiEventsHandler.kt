package com.example.testapp.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.testapp.R
import com.example.testapp.navigation.AppRoute
import com.example.testapp.ui.ui_templates.showShortSnackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun GameUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<GameViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val notEnoughCoins = stringResource(R.string.not_enough_coins)

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                GameUiEvent.NavigateHome -> {
                    delay(150)
                    navController.popBackStack(AppRoute.MenuScreen, inclusive = false)
                }

                GameUiEvent.NavigateToSettings -> {
                    delay(150)
                    navController.navigate(AppRoute.SettingsScreen) {
                        popUpTo<AppRoute.MenuScreen>()
                    }
                }

                GameUiEvent.NotEnoughCoins -> snackbarHostState.showShortSnackbar(notEnoughCoins)
            }
        }
    }

    BackHandler {
        when (viewModel.state.value.dialog) {
            null -> viewModel.onEvent(GameEvent.Pause)
            GameDialog.Pause -> viewModel.onEvent(GameEvent.Resume)
            else -> Unit
        }
    }

    GameScreen(viewModel = viewModel, snackbarHostState = snackbarHostState)
}
