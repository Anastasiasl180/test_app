package com.example.testapp.ui.webview

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.testapp.AppGlobalViewModel
import com.example.testapp.navigation.AppRoute
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel


@Composable
fun WebViewUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<WebViewViewModel>()
    val activity = LocalActivity.current as ComponentActivity
    val globalViewModel = koinViewModel<AppGlobalViewModel>(viewModelStoreOwner = activity)

    DisposableEffect(Unit) {
        globalViewModel.muteMusic()
        onDispose { globalViewModel.unmuteMusic() }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                WebViewUiEvent.NavigateBack -> navController.navigate(AppRoute.SettingsScreen) {
                    popUpTo<AppRoute.MenuScreen>()
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(WebViewEvent.NavigateBack)
    }

    WebViewScreen(viewModel = viewModel)
}
