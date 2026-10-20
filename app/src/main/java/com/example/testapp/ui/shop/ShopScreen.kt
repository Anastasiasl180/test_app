package com.example.testapp.ui.shop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.ui_templates.NSnackbarHost
import com.example.testapp.ui.shop.ui_components.ShopGrid
import com.example.testapp.ui.shop.ui_components.ShopHeader


@Composable
fun ShopScreen(viewModel: ShopViewModel, snackbarHostState: SnackbarHostState) {
    val player by viewModel.player

    Box(modifier = Modifier.fillMaxSize()) {
        NContainer(
            action = { viewModel.onEvent(ShopEvent.NavigateBack) },
            leftImage = R.drawable.icon_back,
            balance = player.balance,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f),
                        contentAlignment = Alignment.Center
                    ) {
                        ShopHeader()
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.62f),
                        contentAlignment = Alignment.Center
                    ) {
                        ShopGrid(
                            skins = viewModel.skins,
                            ownedSkins = player.ownedSkins,
                            equippedSkin = player.equippedSkin,
                            onSkinClick = { viewModel.onEvent(ShopEvent.SelectSkin(it)) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.18f)
                    )
                }
            }
        )

        NSnackbarHost(hostState = snackbarHostState)
    }
}
