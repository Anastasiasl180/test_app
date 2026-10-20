package com.example.testapp.ui.menu.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.BalancePill


@Composable
fun MenuTopBar(
    balance: Int,
    onSoundClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.42f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BalancePill(
            balance = balance,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.35f)
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.38f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NButton(
                onClick = onSoundClick,
                buttonImage = R.drawable.sound_ic,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            NButton(
                onClick = onSettingsClick,
                buttonImage = R.drawable.settings_ic,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
    }
}
