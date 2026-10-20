package com.example.testapp.ui.rules.ui_components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import coil3.compose.AsyncImage
import com.example.testapp.R
import com.example.testapp.notifications.REMINDER_HOUR
import com.example.testapp.ui.game.GameViewModel
import com.example.testapp.ui.game.logic.LevelConfig
import com.example.testapp.notifications.DAILY_REWARD_COINS
import com.example.testapp.ui.ui_templates.SfPro

private val CardBackground = Color(0x992A1411)
private val TitleLight = Color(0xFFF3E4D8)
private val LabelText = Color(0xFFB89A90)

private class Rule(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val text: Int,
    vararg val textArgs: Any,
)

private val rules = listOf(
    Rule(R.drawable.smth_r, R.string.rules_how_to_play, R.string.rules_how_to_play_text),
    Rule(R.drawable.smth_b, R.string.rules_goal, R.string.rules_goal_text, LevelConfig.MOVES),
    Rule(R.drawable.star, R.string.rules_stars, R.string.rules_stars_text),
    Rule(R.drawable.smth_g, R.string.rules_score, R.string.rules_score_text),
    Rule(R.drawable.smth_y, R.string.rules_coins, R.string.rules_coins_text),
    Rule(
        R.drawable.btn_add,
        R.string.rules_daily_reward,
        R.string.rules_daily_reward_text,
        DAILY_REWARD_COINS,
        REMINDER_HOUR
    ),
    Rule(
        R.drawable.smth_v,
        R.string.rules_hint_shuffle,
        R.string.rules_hint_shuffle_text,
        GameViewModel.HINT_COST,
        GameViewModel.SHUFFLE_COST
    ),
    Rule(R.drawable.smth_p, R.string.rules_skins, R.string.rules_skins_text),
    Rule(R.drawable.lvl, R.string.rules_levels, R.string.rules_levels_text, LevelConfig.LEVELS_COUNT),
)

@Composable
fun RulesList() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(0.89f)
            .fillMaxHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        val width = maxWidth
        val titleSize = with(LocalDensity.current) { (width * 0.055f).toSp() }
        val textSize = with(LocalDensity.current) { (width * 0.042f).toSp() }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(width * 0.04f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(rules) { rule ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardBackground, RoundedCornerShape(percent = 10))
                        .padding(horizontal = width * 0.07f, vertical = width * 0.05f),
                    verticalArrangement = Arrangement.spacedBy(width * 0.025f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = rule.icon,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width * 0.11f)
                    )
                    BasicText(
                        text = stringResource(rule.title),
                        style = TextStyle(
                            color = TitleLight,
                            fontFamily = SfPro,
                            fontWeight = FontWeight.Black,
                            fontSize = titleSize,
                            letterSpacing = 0.04.em,
                            textAlign = TextAlign.Center,
                        )
                    )
                    BasicText(
                        text = stringResource(rule.text, *rule.textArgs),
                        style = TextStyle(
                            color = LabelText,
                            fontFamily = SfPro,
                            fontWeight = FontWeight.Bold,
                            fontSize = textSize,
                            lineHeight = 1.4.em,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}
