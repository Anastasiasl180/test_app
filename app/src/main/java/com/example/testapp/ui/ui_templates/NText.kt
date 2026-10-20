package com.example.testapp.ui.ui_templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.testapp.R


@OptIn(ExperimentalTextApi::class)
val SfPro = FontFamily(
    Font(
        R.font.sf_pro,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(FontVariation.weight(900))
    ),
    Font(
        R.font.sf_pro,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
)

@Composable
fun NText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontWeight: FontWeight = FontWeight.Black,
    letterSpacing: Float = 0.02f,
    textAlign: TextAlign = TextAlign.Center,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        BasicText(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(
                minFontSize = 4.sp,
                maxFontSize = 300.sp,
                stepSize = 0.5.sp
            ),
            style = TextStyle(
                color = color,
                fontFamily = SfPro,
                fontWeight = fontWeight,
                letterSpacing = letterSpacing.em,
                textAlign = textAlign,
            )
        )
    }
}
