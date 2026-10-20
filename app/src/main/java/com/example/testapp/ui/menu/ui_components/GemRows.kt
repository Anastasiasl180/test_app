package com.example.testapp.ui.menu.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.testapp.R


private val rowGems = listOf(
    R.drawable.smth_r,
    R.drawable.smth_b,
    R.drawable.smth_g,
    R.drawable.smth_y,
    R.drawable.smth_v,
)

@Composable
 fun GemsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.86f)
            .fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        rowGems.forEach { gem ->
            AsyncImage(
                model = gem,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
    }
}