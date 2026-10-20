package com.example.testapp.ui.levels.ui_components

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
fun LevelsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NText(
            text = stringResource(R.string.levels_title),
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.34f)
                .aspectRatio(4.6f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.18f)
                .aspectRatio(16f)
                .background(UnderlineRed, RoundedCornerShape(percent = 50))
        )
    }
}
