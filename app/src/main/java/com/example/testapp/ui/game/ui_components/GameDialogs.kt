package com.example.testapp.ui.game.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.game.GameDialog
import com.example.testapp.ui.ui_templates.NDialog
import com.example.testapp.ui.ui_templates.NText
import java.util.Locale

private val TitleLight = Color(0xFFF3E4D8)
private val LabelText = Color(0xFFB89A90)
private val PauseRedTop = Color(0xFFD9645A)
private val PauseRedBottom = Color(0xFFA8362C)
private val PauseRing = Color(0xFFF0A79B)

private const val FRAME_RATIO = 920f / 1270f

private const val FRAME_BORDER_TOP = 30f / 1270f

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onSettings: () -> Unit,
    onHome: () -> Unit,
) {
    DialogFrame(
        badgeWidth = 0.18f,
        badgeRatio = 1f,
        badge = { PauseBadge() },
    ) {
        Box(modifier = Modifier.weight(0.11f))

        NText(
            text = stringResource(R.string.pause_title),
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .weight(0.08f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            contentAlignment = Alignment.Center
        ) {
            PauseGems()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.66f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameButton(
                    text = stringResource(R.string.pause_resume),
                    image = R.drawable.btn_light,
                    onClick = onResume,
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                        .aspectRatio(652f / 222f)
                )
                DarkDialogButton(text = stringResource(R.string.pause_restart), onClick = onRestart)
                DarkDialogButton(text = stringResource(R.string.pause_settings), onClick = onSettings)
                DarkDialogButton(text = stringResource(R.string.pause_home), onClick = onHome)
            }
        }

        Box(modifier = Modifier.weight(0.05f))
    }
}

@Composable
fun ResultDialog(
    result: GameDialog.Result,
    onNextLevel: () -> Unit,
    onRetry: () -> Unit,
    onHome: () -> Unit,
) {
    DialogFrame(
        badgeWidth = 0.66f,
        badgeRatio = 660f / 210f,
        heightScale = if (result.hasNextLevel) 1f else 0.8f,
        badge = { ResultBanner(isWin = result.isWin) },
    ) {
        Box(modifier = Modifier.weight(0.1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.17f),
            contentAlignment = Alignment.Center
        ) {
            StarsRow(stars = result.stars)
        }

        NText(
            text = stringResource(R.string.result_final_score),
            color = LabelText,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.1f,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .weight(0.06f)
        )

        NText(
            text = String.format(Locale.US, "%,d", result.score),
            color = TitleLight,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .weight(0.11f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.11f),
            contentAlignment = Alignment.Center
        ) {
            RewardPill(reward = result.reward)
        }

        if (result.hasNextLevel) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                contentAlignment = Alignment.Center
            ) {
                GameButton(
                    text = stringResource(R.string.result_next_level),
                    image = R.drawable.btn_light,
                    onClick = onNextLevel,
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .aspectRatio(652f / 222f)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.14f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .fillMaxHeight(0.62f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameButton(
                    text = stringResource(R.string.result_retry),
                    image = R.drawable.btn_dark,
                    onClick = onRetry,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(652f / 192f)
                )
                GameButton(
                    text = stringResource(R.string.result_home),
                    image = R.drawable.btn_dark,
                    onClick = onHome,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(652f / 192f)
                )
            }
        }

        Box(modifier = Modifier.weight(0.11f))
    }
}

@Composable
private fun DialogFrame(
    badgeWidth: Float,
    badgeRatio: Float,
    heightScale: Float = 1f,
    badge: @Composable BoxScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val frameRatio = FRAME_RATIO / heightScale
    val frameHeight = 1f / frameRatio
    val badgeHeight = badgeWidth / badgeRatio
    val aboveFrame = (badgeHeight / 2 - FRAME_BORDER_TOP * frameHeight).coerceAtLeast(0f)

    NDialog {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f / (aboveFrame + frameHeight))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(frameRatio)
                    .align(Alignment.BottomCenter)
            ) {
               AsyncImage(
                    model = R.drawable.frame,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(badgeWidth)
                    .aspectRatio(badgeRatio)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center,
                content = badge
            )
        }
    }
}

@Composable
private fun DarkDialogButton(text: String, onClick: () -> Unit) {
    GameButton(
        text = text,
        image = R.drawable.btn_dark,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.62f)
            .aspectRatio(652f / 192f)
    )
}

@Composable
private fun PauseBadge() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PauseRedTop, PauseRedBottom)), CircleShape)
            .border(Dp.Hairline, PauseRing, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.36f)
                .fillMaxHeight(0.42f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.32f)
                        .background(TitleLight, RoundedCornerShape(percent = 50))
                )
            }
        }
    }
}

@Composable
private fun PauseGems() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.46f)
            .fillMaxHeight(0.75f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(R.drawable.smth_b, R.drawable.smth_g, R.drawable.smth_y).forEach { gem ->
            AsyncImage(
                model = gem,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
private fun ResultBanner(isWin: Boolean) {
    AsyncImage(
        model = R.drawable.lvl_result,
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.55f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NText(
            text = stringResource(R.string.result_level),
            color = TitleLight,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.1f,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.32f)
        )
        NText(
            text = stringResource(if (isWin) R.string.result_complete else R.string.result_failed),
            color = TitleLight,
            letterSpacing = 0.06f,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.62f)
        )
    }
}

@Composable
private fun StarsRow(stars: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.62f)
            .fillMaxHeight(0.8f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            AsyncImage(
                model = if (index < stars) R.drawable.star else R.drawable.no_star,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(if (index == 1) 1f else 0.75f)
                    .aspectRatio(39f / 37f)
            )
        }
    }
}

@Composable
private fun RewardPill(reward: Int) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .aspectRatio(320f / 116f),
        contentAlignment = Alignment.CenterEnd
    ) {
        AsyncImage(
            model = R.drawable.balance,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        NText(
            text = "+$reward",
            color = Color(0xFFF2C14E),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.38f)
        )
    }
}
