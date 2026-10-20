package com.example.testapp.ui.ui_templates

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import com.example.testapp.ClickSoundPlayer
import com.example.testapp.storage.AppStorage
import org.koin.compose.koinInject


@Composable
fun rememberVibrator(): Vibrator {
    val context = LocalContext.current
    return remember {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
}

fun Vibrator.tryVibrateIfEnabled(paper: AppStorage, durationMs: Long = 100L) {
    val vibro = paper.loadSettings().vibrationEnabled
    if (!vibro) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))

    } else {
        @Suppress("DEPRECATION")
        vibrate(durationMs)
    }
}

@Composable
fun NButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonImage: Int? = null,
    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = ""
    )

    val paper = koinInject<AppStorage>()
    val clickSound = koinInject<ClickSoundPlayer>()
    val vibrator = rememberVibrator()

    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) {
                vibrator.tryVibrateIfEnabled(paper, 100L)
                clickSound.playIfEnabled()
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (buttonImage != null) {
            AsyncImage(
                model = buttonImage,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        content()
    }
}