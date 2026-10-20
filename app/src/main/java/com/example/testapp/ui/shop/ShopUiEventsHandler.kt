package com.example.testapp.ui.shop

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.testapp.navigation.AppRoute
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.showShortSnackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun ShopUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<ShopViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val notEnoughCoins = stringResource(R.string.not_enough_coins)

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                ShopUiEvent.NavigateBack -> {
                    delay(150)
                    navController.popBackStack(AppRoute.MenuScreen, inclusive = false)
                }

                ShopUiEvent.NotEnoughCoins -> snackbarHostState.showShortSnackbar(notEnoughCoins)
            }
        }
    }

    BackHandler {
        viewModel.onEvent(ShopEvent.NavigateBack)
    }

    ShopScreen(viewModel = viewModel, snackbarHostState = snackbarHostState)
}
