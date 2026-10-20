package com.example.testapp.ui.settings.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NText

private val TitleLight = Color(0xFFF3E4D8)
private val UnderlineRed = Color(0xFFC0473A)

@Composable
fun SettingsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NText(
            text = stringResource(R.string.settings_title),
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(6.8f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.18f)
                .aspectRatio(16f)
                .background(UnderlineRed, RoundedCornerShape(percent = 50))
        )
    }
}
