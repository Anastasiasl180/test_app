package com.example.testapp.ui.rules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.rules.ui_components.RulesHeader
import com.example.testapp.ui.rules.ui_components.RulesList


@Composable
fun RulesScreen(viewModel: RulesViewModel) {
    val balance by viewModel.balance

    NContainer(
        action = { viewModel.onEvent(RulesEvent.NavigateBack) },
        leftImage = R.drawable.icon_back,
        balance = balance,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.07f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    RulesHeader()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.75f),
                    contentAlignment = Alignment.Center
                ) {
                    RulesList()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.08f)
                )
            }
        }
    )
}
