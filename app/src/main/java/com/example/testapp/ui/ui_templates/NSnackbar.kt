package com.example.testapp.ui.ui_templates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SNACKBAR_DURATION_MS = 1500L

suspend fun SnackbarHostState.showShortSnackbar(message: String) = coroutineScope {
    launch { showSnackbar(message, duration = SnackbarDuration.Indefinite) }
    delay(SNACKBAR_DURATION_MS)
    currentSnackbarData?.dismiss()
}

private val SnackBackground = Color(0xFF3B1713)
private val SnackBorder = Color(0xFFD9533F)
private val SnackText = Color(0xFFF3E4D8)

@Composable
fun NSnackbarHost(hostState: SnackbarHostState) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier
                .fillMaxWidth(0.84f)
                .fillMaxHeight(0.14f)
        ) { data ->
            val shape = RoundedCornerShape(percent = 50)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(6.5f)
                    .background(SnackBackground, shape)
                    .border(width = Dp.Hairline, color = SnackBorder, shape = shape),
                contentAlignment = Alignment.Center
            ) {
                NText(
                    text = data.visuals.message,
                    color = SnackText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.06f,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.36f)
                )
            }
        }
    }
}
