package com.example.testapp.ui.game.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NText
import java.util.Locale

private val TitleLight = Color(0xFFF3E4D8)
private val LabelText = Color(0xFFB89A90)

@Composable
fun LevelPill(level: Int) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(360f / 84f),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.drawable.lvl_in_game_screen,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        NText(
            text = stringResource(R.string.level_number, level),
            color = TitleLight,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.06f,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.45f)
        )
    }
}

@Composable
fun ScoreRow(score: Int, movesLeft: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.85f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatBox(label = stringResource(R.string.game_score), value = String.format(Locale.US, "%,d", score))
        StatBox(label = stringResource(R.string.game_moves), value = movesLeft.toString())
    }
}

@Composable
private fun StatBox(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(440f / 180f),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.drawable.score_btn,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .fillMaxHeight(0.72f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            NText(
                text = label,
                color = LabelText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.08f,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
            )
            NText(
                text = value,
                color = TitleLight,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            )
        }
    }
}
