package com.example.testapp.ui.ui_templates

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.testapp.ClickSoundPlayer
import com.example.testapp.R
import com.example.testapp.storage.AppStorage
import org.koin.compose.koinInject
import java.util.Locale


@Composable
fun NContainer(
    action: () -> Unit,
    leftImage: Int,
    content: @Composable () -> Unit,
    balance: Int = 0,
    endButton: (@Composable (Modifier) -> Unit)? = null,
) {
    AsyncImage(
        model = R.drawable.bg,
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.Center
    ) { content() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.115f),
        contentAlignment = Alignment.Center
    ) {
        TopBar(
            leftImage = leftImage,
            action = action,
            balance = balance,
            endButton = endButton
        )
    }
}


@Composable
private fun TopBar(
    leftImage: Int,
    balance: Int,
    endButton: (@Composable (Modifier) -> Unit)?,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.42f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            NButton(
                buttonImage = leftImage,
                onClick = action,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
        val endModifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.35f)
        if (endButton != null) {
            endButton(endModifier)
        } else {
            BalancePill(balance = balance, modifier = endModifier)
        }
    }
}

@Composable
fun BalancePill(balance: Int, modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.CenterEnd) {
        AsyncImage(
            model = R.drawable.balance,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.35f))
            BoxWithConstraints(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight(0.36f)
                    .clipToBounds(),
                contentAlignment = Alignment.CenterStart
            ) {
                val fontSize = with(LocalDensity.current) { (maxHeight * 0.85f).toSp() }
                BasicText(
                    text = String.format(Locale.US, "%,d", balance),
                    maxLines = 1,
                    softWrap = false,
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = SfPro,
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize,
                        letterSpacing = 0.02.em,
                    ),
                    modifier = Modifier.basicMarquee()
                )
            }
            Box(modifier = Modifier.weight(0.1f))
        }
    }
}


