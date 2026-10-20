package com.example.testapp.ui.profile.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NButton
import com.example.testapp.ui.ui_templates.NText
import com.example.testapp.ui.ui_templates.SfPro

private val TitleLight = Color(0xFFF3E4D8)
private val Placeholder = Color(0x66F3E4D8)
private val Cursor = Color(0xFFD9533F)
private val PillBackground = Color(0xFF4A1A16)
private val PillText = Color(0xFFE7A08F)

@Composable
fun ProfileAvatar(photoUri: String?, onClick: () -> Unit) {
    NButton(
        onClick = onClick,
        buttonImage = R.drawable.avatar,
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .aspectRatio(1f)
    ) {
        if (photoUri != null) {
            AsyncImage(
                model = photoUri,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(0.66f)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun ProfileName(name: String, level: Int, onNameChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameField(
            name = name,
            onNameChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(14f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.22f)
                .aspectRatio(3.6f)
                .background(PillBackground, RoundedCornerShape(percent = 50)),
            contentAlignment = Alignment.Center
        ) {
            NText(
                text = stringResource(R.string.level_number, level),
                color = PillText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.08f,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight(0.5f)
            )
        }
    }
}

@Composable
private fun NameField(name: String, onNameChange: (String) -> Unit, modifier: Modifier) {
    val focusManager = LocalFocusManager.current
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val fontSize = with(LocalDensity.current) { (maxHeight * 0.72f).toSp() }
        val style = TextStyle(
            fontFamily = SfPro,
            fontWeight = FontWeight.Black,
            fontSize = fontSize,
            letterSpacing = 0.04.em,
            textAlign = TextAlign.Center,
        )
        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            singleLine = true,
            textStyle = style.copy(color = TitleLight),
            cursorBrush = SolidColor(Cursor),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.Center) {
                    if (name.isEmpty()) {
                        BasicText(
                            text = stringResource(R.string.profile_enter_name),
                            maxLines = 1,
                            style = style.copy(color = Placeholder),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    inner()
                }
            }
        )
    }
}
