package com.example.testapp.ui.profile

import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.testapp.navigation.AppRoute
import com.example.testapp.ui.ui_templates.showShortSnackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<ProfileViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                ProfileUiEvent.NavigateBack -> {
                    delay(150)
                    navController.popBackStack(AppRoute.MenuScreen, inclusive = false)
                }

            }
        }
    }

    BackHandler {
        viewModel.onEvent(ProfileEvent.NavigateBack)
    }

    ProfileScreen(viewModel = viewModel, snackbarHostState = snackbarHostState)
}
