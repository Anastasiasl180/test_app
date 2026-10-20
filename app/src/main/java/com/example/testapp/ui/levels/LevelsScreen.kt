package com.example.testapp.ui.levels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.testapp.R
import com.example.testapp.ui.game.ui_components.GameButton
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.levels.ui_components.LevelsGrid
import com.example.testapp.ui.levels.ui_components.LevelsHeader


@Composable
fun LevelsScreen(viewModel: LevelsViewModel) {
    val passedLevels by viewModel.passedLevels
    val levelStars by viewModel.levelStars

    NContainer(
        action = { viewModel.onEvent(LevelsEvent.NavigateBack) },
        leftImage = R.drawable.icon_back,
        endButton = { modifier ->
            GameButton(
                text = stringResource(R.string.rules_button),
                image = R.drawable.btn_dark,
                onClick = { viewModel.onEvent(LevelsEvent.NavigateToRules) },
                modifier = modifier
            )
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.07f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    LevelsHeader()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.83f),
                    contentAlignment = Alignment.Center
                ) {
                    LevelsGrid(
                        levels = viewModel.allLevels,
                        passedLevels = passedLevels,
                        levelStars = levelStars,
                        onLevelClick = { viewModel.onEvent(LevelsEvent.NavigateToGame(it)) }
                    )
                }
            }
        }
    )
}
