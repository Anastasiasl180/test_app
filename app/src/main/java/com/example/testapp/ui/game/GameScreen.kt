package com.example.testapp.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.R
import com.example.testapp.ui.game.ui_components.GameBoard
import com.example.testapp.ui.game.ui_components.GameButtons
import com.example.testapp.ui.game.ui_components.GoalBar
import com.example.testapp.ui.game.ui_components.LevelPill
import com.example.testapp.ui.game.ui_components.PauseDialog
import com.example.testapp.ui.game.ui_components.ScoreRow
import com.example.testapp.ui.game.ui_components.ResultDialog
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.ui_templates.NSnackbarHost


@Composable
fun GameScreen(viewModel: GameViewModel, snackbarHostState: SnackbarHostState) {
    val state by viewModel.state

    Box(modifier = Modifier.fillMaxSize()) {
        NContainer(
            action = { viewModel.onEvent(GameEvent.Pause) },
            leftImage = R.drawable.pause,
            balance = state.balance,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.09f),
                        contentAlignment = Alignment.Center
                    ) {
                        ScoreRow(score = state.score, movesLeft = state.movesLeft)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.43f),
                        contentAlignment = Alignment.Center
                    ) {
                        GameBoard(
                            rows = state.rows,
                            cols = state.cols,
                            gems = state.gems,
                            selected = state.selected,
                            hintIds = state.hintIds,
                            matchedIds = state.matchedIds,
                            onTap = { viewModel.onEvent(GameEvent.TapCell(it)) },
                            onSwipe = { from, to -> viewModel.onEvent(GameEvent.SwapGems(from, to)) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.08f),
                        contentAlignment = Alignment.Center
                    ) {
                        GoalBar(goal = state.goal)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.09f),
                        contentAlignment = Alignment.Center
                    ) {
                        GameButtons(
                            onHint = { viewModel.onEvent(GameEvent.Hint) },
                            onShuffle = { viewModel.onEvent(GameEvent.Shuffle) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.21f)
                    )
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.115f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.42f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(0.15f))
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    LevelPill(level = state.level)
                }
                Box(modifier = Modifier.weight(0.35f))
            }
        }

        when (val dialog = state.dialog) {
            GameDialog.Pause -> PauseDialog(
                onResume = { viewModel.onEvent(GameEvent.Resume) },
                onRestart = { viewModel.onEvent(GameEvent.Restart) },
                onSettings = { viewModel.onEvent(GameEvent.OpenSettings) },
                onHome = { viewModel.onEvent(GameEvent.NavigateHome) }
            )

            is GameDialog.Result -> ResultDialog(
                result = dialog,
                onNextLevel = { viewModel.onEvent(GameEvent.NextLevel) },
                onRetry = { viewModel.onEvent(GameEvent.Restart) },
                onHome = { viewModel.onEvent(GameEvent.NavigateHome) }
            )

            null -> Unit
        }

        NSnackbarHost(hostState = snackbarHostState)
    }
}
