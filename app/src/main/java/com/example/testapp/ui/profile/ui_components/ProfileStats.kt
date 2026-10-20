package com.example.testapp.ui.profile.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NText
import java.util.Locale

private val CardBackground = Color(0x992A1411)
private val TitleLight = Color(0xFFF3E4D8)
private val LabelText = Color(0xFFB89A90)

@Composable
fun ProfileStats(levels: Int, bestScore: Int, coins: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.89f)
            .fillMaxHeight(0.85f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatCard(icon = R.drawable.smth_r, value = levels.toString(), label = stringResource(R.string.profile_levels))
        StatCard(icon = R.drawable.star, value = formatNumber(bestScore), label = stringResource(R.string.profile_best))
        StatCard(icon = R.drawable.smth_v, value = formatNumber(coins), label = stringResource(R.string.profile_coins))
    }
}

@Composable
private fun StatCard(icon: Int, value: String, label: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1.43f)
            .background(CardBackground, RoundedCornerShape(percent = 16)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .fillMaxHeight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.36f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = icon,
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .aspectRatio(1f)
                )
            }
            NText(
                text = value,
                color = TitleLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            )
            NText(
                text = label,
                color = LabelText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.24f)
            )
        }
    }
}

private fun formatNumber(value: Int): String = String.format(Locale.US, "%,d", value)
