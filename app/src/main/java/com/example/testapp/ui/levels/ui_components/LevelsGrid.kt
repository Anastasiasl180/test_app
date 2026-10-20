package com.example.testapp.ui.levels.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.BiasAlignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText

private const val LEVELS_IN_ROW = 3

private enum class LevelStatus {
    Completed,
    Next,
    Locked
}

@Composable
fun LevelsGrid(
    levels: List<Int>,
    passedLevels: Set<Int>,
    levelStars: Map<Int, Int>,
    onLevelClick: (Int) -> Unit,
) {
    val nextLevel = (passedLevels.maxOrNull() ?: 0) + 1
    val rows = levels.chunked(LEVELS_IN_ROW)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = ((nextLevel - 1) / LEVELS_IN_ROW - 1).coerceIn(0, rows.lastIndex)
    )

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(0.74f)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(rows) { rowLevels ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3.2f),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3.5f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowLevels.forEach { level ->
                        LevelTile(
                            number = level,
                            stars = levelStars[level] ?: 0,
                            status = when {
                                level in passedLevels -> LevelStatus.Completed
                                level == nextLevel -> LevelStatus.Next
                                else -> LevelStatus.Locked
                            },
                            onClick = { onLevelClick(level) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelTile(number: Int, stars: Int, status: LevelStatus, onClick: () -> Unit) {
    val isLocked = status == LevelStatus.Locked

    NButton(
        onClick = onClick,
        enabled = !isLocked,
        buttonImage = when (status) {
            LevelStatus.Completed -> R.drawable.lvl
            LevelStatus.Next -> R.drawable.fail_lvl
            LevelStatus.Locked -> R.drawable.cl_lvl
        },
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(232f / 235f)
            .alpha(if (isLocked) 0.6f else 1f)
    ) {
        if (!isLocked) {
            NText(
                text = number.toString(),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.3f)
                    .align(BiasAlignment(0f, 0.05f))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.15f)
                    .align(BiasAlignment(0f, 0.62f)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    AsyncImage(
                        model = if (index < stars) R.drawable.star else R.drawable.no_star,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(39f / 37f)
                    )
                }
            }
        }
    }
}
