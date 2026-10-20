package com.example.testapp.ui.shop.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.game.logic.Skin
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText

private const val SKINS_IN_ROW = 2

private val TitleLight = Color(0xFFF3E4D8)
private val BadgeGreen = Color(0xFF4E8F55)
private val PriceYellow = Color(0xFFF2C14E)

@Composable
fun ShopGrid(
    skins: List<Skin>,
    ownedSkins: Set<Skin>,
    equippedSkin: Skin,
    onSkinClick: (Skin) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.97f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        skins.chunked(SKINS_IN_ROW).forEach { rowSkins ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.25f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowSkins.forEach { skin ->
                    SkinCard(
                        skin = skin,
                        isOwned = skin in ownedSkins,
                        isEquipped = skin == equippedSkin,
                        onClick = { onSkinClick(skin) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkinCard(
    skin: Skin,
    isOwned: Boolean,
    isEquipped: Boolean,
    onClick: () -> Unit,
) {
    NButton(
        onClick = onClick,
        buttonImage = skin.card,
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(492f / 452f)
    ) {
        NText(
            text = skin.title,
            color = TitleLight,
            letterSpacing = 0.04f,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.1f)
                .align(BiasAlignment(0f, 0.4f))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.56f)
                .fillMaxHeight(0.13f)
                .align(BiasAlignment(0f, 0.74f)),
            contentAlignment = Alignment.Center
        ) {
            when {
                isEquipped -> SkinBadge(text = stringResource(R.string.skin_equipped))
                isOwned -> SkinBadge(text = stringResource(R.string.skin_owned))
                else -> SkinPrice(price = skin.price)
            }
        }
    }
}

@Composable
private fun SkinBadge(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BadgeGreen, RoundedCornerShape(percent = 50)),
        contentAlignment = Alignment.Center
    ) {
        NText(
            text = text,
            color = TitleLight,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.08f,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.5f)
        )
    }
}

@Composable
private fun SkinPrice(price: Int) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(320f / 116f),
        contentAlignment = Alignment.CenterEnd
    ) {
        AsyncImage(
            model = R.drawable.balance,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        NText(
            text = price.toString(),
            color = PriceYellow,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.45f)
        )
    }
}
