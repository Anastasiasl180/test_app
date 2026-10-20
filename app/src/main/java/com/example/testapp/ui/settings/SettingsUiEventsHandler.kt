package com.example.testapp.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.testapp.AppLanguage
import com.example.testapp.navigation.AppRoute
import com.example.testapp.AppGlobalViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<SettingsViewModel>()
    val activity = LocalActivity.current as ComponentActivity
    val globalViewModel = koinViewModel<AppGlobalViewModel>(viewModelStoreOwner = activity)

    LaunchedEffect(Unit) {
        viewModel.event.collect { effect ->
            when (effect) {
                is SettingsEffect.MusicToggled -> globalViewModel.togglePlayback()
                SettingsEffect.NavigateAway -> {
                    delay(150)
                    navController.popBackStack(AppRoute.MenuScreen, inclusive = false)
                }

                SettingsEffect.OpenWebView -> {
                    delay(150)
                    navController.navigate(AppRoute.WebViewPart) {
                        popUpTo<AppRoute.MenuScreen>()
                    }
                }
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(SettingsIntent.Confirm)
    }

    val notificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.handleIntent(SettingsIntent.SetNotifications(granted))
    }

    val language = LocalConfiguration.current.locales[0].language

    SettingsScreen(
        viewModel = viewModel,
        onLanguageClick = {
            AppLanguage.set(if (language == AppLanguage.UKRAINIAN) AppLanguage.ENGLISH else AppLanguage.UKRAINIAN)
        },
        onNotificationsChange = { on ->
            val needsPermission = on && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED
            if (needsPermission) {
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.handleIntent(SettingsIntent.SetNotifications(on))
            }
        }
    )
}
