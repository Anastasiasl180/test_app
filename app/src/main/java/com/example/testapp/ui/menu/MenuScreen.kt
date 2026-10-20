package com.example.testapp.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NSnackbarHost
import com.example.testapp.ui.menu.ui_components.DailyRewardButton
import com.example.testapp.ui.menu.ui_components.GemsRow
import com.example.testapp.ui.menu.ui_components.MenuButtons
import com.example.testapp.ui.menu.ui_components.MenuHeader
import com.example.testapp.ui.menu.ui_components.MenuTopBar


@Composable
fun MenuScreen(
    viewModel: MenuViewModel,
    snackbarHostState: SnackbarHostState,
    onDailyRewardClick: () -> Unit,
    onSoundClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = R.drawable.bg,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.115f),
                contentAlignment = Alignment.Center
            ) {
                MenuTopBar(
                    balance = state.balance,
                    onSoundClick = onSoundClick,
                    onSettingsClick = { viewModel.onAction(MenuAction.NavigateToSettings) }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.295f),
                contentAlignment = Alignment.Center
            ) {
                MenuHeader()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                contentAlignment = Alignment.Center
            ) {
                GemsRow()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f),
                contentAlignment = Alignment.Center
            ) {
                MenuButtons(
                    onPlay = { viewModel.onAction(MenuAction.NavigateToPlay) },
                    onLevels = { viewModel.onAction(MenuAction.NavigateToLevels) },
                    onShop = { viewModel.onAction(MenuAction.NavigateToShop) },
                    onProfile = { viewModel.onAction(MenuAction.NavigateToProfile) }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                contentAlignment = Alignment.Center
            ) {
                DailyRewardButton(isAvailable = state.dailyRewardAvailable, onClick = onDailyRewardClick)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.04f)
            )
        }

        NSnackbarHost(hostState = snackbarHostState)
    }
}



