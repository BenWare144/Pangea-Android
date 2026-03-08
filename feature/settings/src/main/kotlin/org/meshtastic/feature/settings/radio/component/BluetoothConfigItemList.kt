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
package org.meshtastic.feature.settings.radio.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.meshtastic.core.strings.Res
import org.meshtastic.core.strings.bluetooth
import org.meshtastic.core.strings.bluetooth_config
import org.meshtastic.core.strings.bluetooth_enabled
import org.meshtastic.core.strings.enable_expert_mode_to_edit
import org.meshtastic.core.strings.fixed_pin
import org.meshtastic.core.strings.pairing_mode
import org.meshtastic.core.ui.component.DropDownPreference
import org.meshtastic.core.ui.component.EditTextPreference
import org.meshtastic.core.ui.component.SwitchPreference
import org.meshtastic.core.ui.component.TitledCard
import org.meshtastic.core.ui.theme.StatusColors.StatusOrange
import org.meshtastic.feature.settings.radio.RadioConfigViewModel
import org.meshtastic.proto.Config

@Composable
fun BluetoothConfigScreen(viewModel: RadioConfigViewModel = hiltViewModel(), onBack: () -> Unit) {
    val state by viewModel.radioConfigState.collectAsStateWithLifecycle()
    val expertModeEnabled by viewModel.expertModeEnabled.collectAsStateWithLifecycle()
    val bluetoothConfig = state.radioConfig.bluetooth ?: Config.BluetoothConfig()
    val formState = rememberConfigState(initialValue = bluetoothConfig)
    val focusManager = LocalFocusManager.current

    RadioConfigScreenList(
        title = stringResource(Res.string.bluetooth),
        onBack = onBack,
        configState = formState,
        enabled = state.connected,
        responseState = state.responseState,
        onDismissPacketResponse = viewModel::clearPacketResponse,
        onSave = {
            val config = Config(bluetooth = it)
            viewModel.setConfig(config)
        },
    ) {
        item {
            TitledCard(title = stringResource(Res.string.bluetooth_config)) {
                SwitchPreference(
                    title = stringResource(Res.string.bluetooth_enabled),
                    checked = formState.value.enabled,
                    enabled = state.connected,
                    onCheckedChange = { formState.value = formState.value.copy(enabled = it) },
                    containerColor = CardDefaults.cardColors().containerColor,
                )
                HorizontalDivider()
                DropDownPreference(
                    title = stringResource(Res.string.pairing_mode),
                    enabled = state.connected && expertModeEnabled,
                    items =
                    Config.BluetoothConfig.PairingMode.entries
                        .filter { it.name != "UNRECOGNIZED" }
                        .map { it to it.name },
                    selectedItem =
                    formState.value.mode?.takeUnless { it.name == "UNRECOGNIZED" }
                        ?: Config.BluetoothConfig.PairingMode.RANDOM_PIN,
                    onItemSelected = { formState.value = formState.value.copy(mode = it) },
                )
                HorizontalDivider()
                EditTextPreference(
                    title = stringResource(Res.string.fixed_pin),
                    value = formState.value.fixed_pin ?: 0,
                    enabled = state.connected && expertModeEnabled,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    onValueChanged = {
                        if (it.toString().length == 6) { // ensure 6 digits
                            formState.value = formState.value.copy(fixed_pin = it)
                        }
                    },
                )
                if (!expertModeEnabled) {
                    HorizontalDivider()
                    Text(
                        text = stringResource(Res.string.enable_expert_mode_to_edit),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.StatusOrange,
                    )
                }
            }
        }
    }
}
