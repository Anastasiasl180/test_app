package com.example.testapp.ui.menu.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NText

private val TitleLight = Color(0xFFF3E4D8)
private val TitleRed = Color(0xFFD9533F)
private val TagBackground = Color(0xFF3B1713)
private val TagText = Color(0xFFEBD3C6)

@Composable
 fun MenuHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoCluster(
            modifier = Modifier
                .fillMaxWidth(0.42f)
                .aspectRatio(480f / 440f)
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.34f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NText(
                text = stringResource(R.string.brand_painite),
                color = TitleLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4.6f)
            )
            NText(
                text = stringResource(R.string.brand_gear),
                color = TitleRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4.6f)
            )
        }
        TagPill(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .aspectRatio(5.9f)
        )
    }
}

private class LogoGem(
    val image: Int,
    val centerX: Float,
    val centerY: Float,
    val size: Float,
)

private val logoGems = listOf(
    LogoGem(R.drawable.smth_r, centerX = 0.41f, centerY = 0.38f, size = 0.41f),
    LogoGem(R.drawable.smth_g, centerX = 0.78f, centerY = 0.47f, size = 0.32f),
    LogoGem(R.drawable.smth_y, centerX = 0.22f, centerY = 0.70f, size = 0.29f),
    LogoGem(R.drawable.smth_v, centerX = 0.66f, centerY = 0.79f, size = 0.25f),
)

@Composable
private fun LogoCluster(modifier: Modifier) {
    BoxWithConstraints(modifier = modifier) {
        AsyncImage(
            model = R.drawable.ellipse,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.18f)
        )
        logoGems.forEach { gem ->
            val gemSize = maxWidth * gem.size
            AsyncImage(
                model = gem.image,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .offset(
                        x = maxWidth * gem.centerX - gemSize / 2,
                        y = maxHeight * gem.centerY - gemSize / 2
                    )
                    .size(gemSize)
            )
        }
    }
}

@Composable
private fun TagPill(modifier: Modifier) {
    Box(
        modifier = modifier.background(TagBackground, RoundedCornerShape(percent = 50)),
        contentAlignment = Alignment.Center
    ) {
        NText(
            text = stringResource(R.string.menu_tag),
            color = TagText,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.08f,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.55f)
        )
    }
}