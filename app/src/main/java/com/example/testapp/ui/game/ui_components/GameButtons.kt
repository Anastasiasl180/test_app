package com.example.testapp.ui.game.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText

private val TitleLight = Color(0xFFF3E4D8)

@Composable
fun GameButtons(
    onHint: () -> Unit,
    onShuffle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.74f)
            .fillMaxHeight(0.66f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameButton(
            text = stringResource(R.string.game_hint),
            image = R.drawable.btn_light,
            onClick = onHint,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(652f / 222f)
        )
        GameButton(
            text = stringResource(R.string.game_shuffle),
            image = R.drawable.btn_dark,
            onClick = onShuffle,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2.5f)
        )
    }
}

@Composable
fun GameButton(
    text: String,
    image: Int,
    modifier: Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    NButton(
        onClick = onClick,
        enabled = enabled,
        buttonImage = image,
        modifier = modifier.alpha(if (enabled) 1f else 0.5f)
    ) {
        NText(
            text = text,
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.28f)
        )
    }
}
