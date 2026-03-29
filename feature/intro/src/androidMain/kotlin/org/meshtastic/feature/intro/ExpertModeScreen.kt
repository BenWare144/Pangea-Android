/*
 * Copyright (c) 2026 Meshtastic LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.meshtastic.feature.intro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.meshtastic.core.resources.Res
import org.meshtastic.core.resources.expert_mode_intro_description_one
import org.meshtastic.core.resources.expert_mode_intro_description_three
import org.meshtastic.core.resources.expert_mode_intro_description_two
import org.meshtastic.core.resources.expert_mode_intro_title
import org.meshtastic.core.resources.next

@Composable
internal fun ExpertModeScreen(onContinue: () -> Unit) {
    val features = remember {
        listOf(
            FeatureUIData(
                icon = Icons.Outlined.Tune,
                subtitleRes = Res.string.expert_mode_intro_description_one,
            ),
            FeatureUIData(
                icon = Icons.Outlined.Build,
                subtitleRes = Res.string.expert_mode_intro_description_two,
            ),
            FeatureUIData(
                icon = Icons.Outlined.Handyman,
                subtitleRes = Res.string.expert_mode_intro_description_three,
            ),
        )
    }

    Scaffold(
        bottomBar = {
            IntroBottomBar(
                onSkip = {},
                onConfigure = onContinue,
                skipButtonText = "",
                configureButtonText = stringResource(Res.string.next),
                showSkipButton = false,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.expert_mode_intro_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            features.forEach { feature ->
                FeatureRow(feature = feature)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ExpertModeScreenPreview() {
    ExpertModeScreen(onContinue = {})
}
