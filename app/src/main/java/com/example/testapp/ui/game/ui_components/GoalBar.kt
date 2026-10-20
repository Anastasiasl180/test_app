package com.example.testapp.ui.game.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.game.GoalProgress
import com.example.testapp.ui.ui_templates.NText

private val LabelText = Color(0xFFB89A90)

@Composable
fun GoalBar(goal: GoalProgress) {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .aspectRatio(800f / 140f),
        contentAlignment = Alignment.Center
    ) {
       AsyncImage(
            model = R.drawable.goal_bar,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .fillMaxHeight(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NText(
                text = stringResource(R.string.game_goal),
                color = LabelText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.08f,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .aspectRatio(3f)
            )
            GoalItem(goal)
        }
    }
}

@Composable
private fun GoalItem(goal: GoalProgress) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(2.2f)
            .alpha(if (goal.isDone) 0.5f else 1f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = goal.type.image,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        NText(
            text = if (goal.isDone) "✓" else "x${goal.left}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .aspectRatio(1.1f)
        )
    }
}
