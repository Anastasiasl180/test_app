package com.example.testapp.ui.menu

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.example.testapp.AppGlobalViewModel
import com.example.testapp.R
import com.example.testapp.navigation.AppRoute
import com.example.testapp.notifications.DAILY_REWARD_COINS
import com.example.testapp.ui.ui_templates.showShortSnackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun MenuUiEventsHandler(navController: NavController) {

    val viewModel = koinViewModel<MenuViewModel>()
    val activity = LocalActivity.current as ComponentActivity
    val globalViewModel = koinViewModel<AppGlobalViewModel>(viewModelStoreOwner = activity)
    val snackbarHostState = remember { SnackbarHostState() }
    val claimedText = stringResource(R.string.daily_reward_claimed, DAILY_REWARD_COINS)
    val notReadyTemplate = stringResource(R.string.daily_reward_not_ready)
    val hoursMinutesTemplate = stringResource(R.string.time_hours_minutes)
    val minutesTemplate = stringResource(R.string.time_minutes)

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshBalance()
    }

    val scope = rememberCoroutineScope()
    var snackbarJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navEvents.collectLatest { event ->
            delay(150)
            when (event) {
                MenuNavEvent.NavigateToGame -> navController.navigate(AppRoute.GameScreen)
                MenuNavEvent.NavigateToLevels -> navController.navigate(AppRoute.LevelScreen)
                MenuNavEvent.NavigateToShop -> navController.navigate(AppRoute.ShopScreen)
                MenuNavEvent.NavigateToProfile -> navController.navigate(AppRoute.ProfileScreen)
                MenuNavEvent.NavigateToSettings -> navController.navigate(AppRoute.SettingsScreen)
            }
        }
    }

    BackHandler {
        activity.finish()
    }

    MenuScreen(
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        onDailyRewardClick = {
            val message = if (viewModel.claimDailyReward()) {
                claimedText
            } else {
                val timeLeft = viewModel.dailyRewardTimeLeft()
                val hours = timeLeft / 3_600_000
                val minutes = (timeLeft % 3_600_000 / 60_000).coerceAtLeast(if (hours == 0L) 1 else 0)
                val time = if (hours > 0) hoursMinutesTemplate.format(hours, minutes) else minutesTemplate.format(minutes)
                notReadyTemplate.format(time)
            }
            snackbarJob?.cancel()
            snackbarJob = scope.launch { snackbarHostState.showShortSnackbar(message) }
        },
        onSoundClick = {
            globalViewModel.togglePlayback()
            globalViewModel.persistCurrentPrefs()
        }
    )
}
