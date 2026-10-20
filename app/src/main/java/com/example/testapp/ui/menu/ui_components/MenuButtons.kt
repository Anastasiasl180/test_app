package com.example.testapp.ui.menu.ui_components

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText

private val TitleLight = Color(0xFFF3E4D8)
private val DailyRewardText = Color(0xFFC8A79C)
@Composable
 fun MenuButtons(
    onPlay: () -> Unit,
    onLevels: () -> Unit,
    onShop: () -> Unit,
    onProfile: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuButton(
            text = stringResource(R.string.menu_play),
            image = R.drawable.btn_light,
            textHeight = 0.3f,
            modifier = Modifier
                .fillMaxWidth(0.52f)
                .aspectRatio(652f / 222f),
            onClick = onPlay
        )
        MenuButton(
            text = stringResource(R.string.menu_levels),
            image = R.drawable.btn_dark,
            textHeight = 0.26f,
            modifier = Modifier
                .fillMaxWidth(0.52f)
                .aspectRatio(652f / 192f),
            onClick = onLevels
        )
        MenuButton(
            text = stringResource(R.string.menu_shop),
            image = R.drawable.btn_dark,
            textHeight = 0.26f,
            modifier = Modifier
                .fillMaxWidth(0.52f)
                .aspectRatio(652f / 192f),
            onClick = onShop
        )
        MenuButton(
            text = stringResource(R.string.menu_profile),
            image = R.drawable.btn_dark,
            textHeight = 0.26f,
            modifier = Modifier
                .fillMaxWidth(0.52f)
                .aspectRatio(652f / 192f),
            onClick = onProfile
        )
    }
}

@Composable
private fun MenuButton(
    text: String,
    image: Int,
    textHeight: Float,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    NButton(
        onClick = onClick,
        buttonImage = image,
        modifier = modifier
    ) {
        NText(
            text = text,
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(textHeight)
        )
    }
}

@Composable
fun DailyRewardButton(isAvailable: Boolean, onClick: () -> Unit) {
    NButton(
        onClick = onClick,
        buttonImage = R.drawable.btn_daily_rew,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .aspectRatio(5f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.1f))
            NText(
                text = stringResource(R.string.daily_reward),
                color = DailyRewardText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.04f,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight(0.28f)
            )
            Box(modifier = Modifier.weight(0.25f))
            AsyncImage(
                model = R.drawable.btn_add,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .aspectRatio(1f)
                    .alpha(if (isAvailable) 1f else 0f)
            )
            Box(modifier = Modifier.weight(0.08f))
        }
    }
}
