/*
 * Copyright (c) 2025-2026 Meshtastic LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.meshtastic.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.util.withContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.meshtastic.core.strings.Res
import org.meshtastic.core.strings.acknowledgements
import org.meshtastic.core.strings.expert_mode
import org.meshtastic.core.strings.expert_mode_disabled
import org.meshtastic.core.strings.expert_mode_enabled_full_settings_unlocked
import org.meshtastic.core.strings.meshtastic_app_name
import org.meshtastic.core.strings.taps_to_expert_mode
import org.meshtastic.core.ui.component.MainAppBar
import org.meshtastic.core.ui.theme.StatusColors.StatusOrange
import org.meshtastic.core.ui.util.showToast
import kotlin.time.Duration.Companion.seconds

@Composable
fun AboutScreen(onNavigateUp: () -> Unit, viewModel: AboutViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val expertModeEnabled by viewModel.expertModeEnabled.collectAsStateWithLifecycle()
    var tapCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(tapCount) {
        if (tapCount in 1..<7) {
            delay(3.seconds)
            tapCount = 0
        }
    }

    Scaffold(
        topBar = {
            MainAppBar(
                title = stringResource(Res.string.acknowledgements),
                canNavigateUp = true,
                onNavigateUp = onNavigateUp,
                ourNode = null,
                showNodeChip = false,
                actions = {},
                onClickChip = {},
            )
        },
    ) { paddingValues ->
        val libraries = remember {
            try {
                Libs.Builder().withContext(context).build()
            } catch (e: IllegalStateException) {
                Logger.w("${e.message}")
                null
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = stringResource(Res.string.meshtastic_app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = viewModel.appVersionName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).clickable {
                    tapCount += 1
                    if (tapCount >= 7) {
                        val updated = !expertModeEnabled
                        viewModel.setExpertModeEnabled(updated)
                        hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        tapCount = 0
                        scope.launch {
                            context.showToast(
                                getString(
                                    if (updated) {
                                        Res.string.expert_mode_enabled_full_settings_unlocked
                                    } else {
                                        Res.string.expert_mode_disabled
                                    },
                                ),
                            )
                        }
                    } else if (tapCount >= 4) {
                        scope.launch {
                            context.showToast(
                                getString(
                                    Res.string.taps_to_expert_mode,
                                    (7 - tapCount).toString(),
                                ),
                            )
                        }
                    }
                },
            )

            if (expertModeEnabled) {
                Icon(
                    imageVector = Icons.Rounded.Build,
                    contentDescription = stringResource(Res.string.expert_mode),
                    tint = MaterialTheme.colorScheme.StatusOrange,
                    modifier = Modifier.padding(horizontal = 16.dp).size(18.dp),
                )
            }

            if (libraries != null) {
                LibrariesContainer(
                    showAuthor = true,
                    showVersion = true,
                    showDescription = true,
                    showLicenseBadges = true,
                    showFundingBadges = true,
                    modifier = Modifier.fillMaxSize(),
                    libraries = libraries,
                )
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    MaterialTheme { AboutScreen(onNavigateUp = {}) }
}
