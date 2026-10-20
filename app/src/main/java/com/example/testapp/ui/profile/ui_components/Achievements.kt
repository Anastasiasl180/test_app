package com.example.testapp.ui.profile.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
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
import com.example.testapp.ui.game.logic.Achievement
import com.example.testapp.ui.ui_templates.NText

private val TitleLight = Color(0xFFF3E4D8)
private val LabelText = Color(0xFFB89A90)
private val DoneGreen = Color(0xFF4E8F55)

@Composable
fun AchievementsTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(0.5f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NText(
            text = stringResource(R.string.profile_achievements),
            color = TitleLight,
            letterSpacing = 0.06f,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(9f)
        )
    }
}

@Composable
fun AchievementsList(achievements: List<Achievement>, unlocked: Set<Achievement>) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.89f)
            .fillMaxHeight(0.95f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        achievements.forEach { achievement ->
            AchievementRow(achievement = achievement, isUnlocked = achievement in unlocked)
        }
    }
}

@Composable
private fun AchievementRow(achievement: Achievement, isUnlocked: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(976f / 156f),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.drawable.settings_field,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.03f))
            AsyncImage(
                model = achievement.icon,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.66f)
                    .aspectRatio(1f)
                    .alpha(if (isUnlocked) 1f else 0.35f)
            )
            Box(modifier = Modifier.weight(0.04f))
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxHeight(0.6f)
            ) {
                NText(
                    text = stringResource(achievement.title),
                    color = TitleLight,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0f,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.56f)
                )
                NText(
                    text = stringResource(achievement.description, *achievement.descriptionArgs.toTypedArray()),
                    color = LabelText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0f,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.44f)
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.14f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.36f)
                            .aspectRatio(1f)
                            .background(DoneGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        NText(
                            text = "✓",
                            color = TitleLight,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(0.6f)
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(0.03f))
        }
    }
}
