/*
 * Copyright (c) 2026 Meshtastic LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.meshtastic.feature.settings.navigation

import org.meshtastic.core.navigation.Route
import org.meshtastic.core.navigation.SettingsRoutes

/** Maps `/settings/<segment>` deep links to [SettingsRoutes]. */
fun settingsRouteFromDeepLinkSegment(segment: String): Route? =
    when (segment.lowercase()) {
        "user" -> SettingsRoutes.User
        "channels", "channel", "channel-config" -> SettingsRoutes.ChannelConfig
        "device" -> SettingsRoutes.Device
        "position" -> SettingsRoutes.Position
        "power" -> SettingsRoutes.Power
        "network" -> SettingsRoutes.Network
        "display" -> SettingsRoutes.Display
        "lora" -> SettingsRoutes.LoRa
        "bluetooth" -> SettingsRoutes.Bluetooth
        "security" -> SettingsRoutes.Security
        "mqtt" -> SettingsRoutes.MQTT
        "serial" -> SettingsRoutes.Serial
        "externalnotification", "external-notification", "extnotification", "ext-notification" ->
            SettingsRoutes.ExtNotification
        "storeandforward", "store-forward", "storeforward" -> SettingsRoutes.StoreForward
        "rangetest", "range-test" -> SettingsRoutes.RangeTest
        "telemetry" -> SettingsRoutes.Telemetry
        "cannedmessages", "canned-messages", "cannedmessage", "canned-message" -> SettingsRoutes.CannedMessage
        "audio", "ringtone" -> SettingsRoutes.Audio
        "remotehardware", "remote-hardware" -> SettingsRoutes.RemoteHardware
        "neighborinfo", "neighbor-info" -> SettingsRoutes.NeighborInfo
        "ambientlighting", "ambient-lighting" -> SettingsRoutes.AmbientLighting
        "detectionsensor", "detection-sensor" -> SettingsRoutes.DetectionSensor
        "paxcounter", "pax-counter" -> SettingsRoutes.Paxcounter
        "statusmessage", "status-message" -> SettingsRoutes.StatusMessage
        "trafficmanagement", "traffic-management" -> SettingsRoutes.TrafficManagement
        "tak" -> SettingsRoutes.TAK
        "administration", "admin" -> SettingsRoutes.Administration
        "debug", "debuglogs", "debug-logs" -> SettingsRoutes.DebugPanel
        "cleannodedb", "clean-node-db" -> SettingsRoutes.CleanNodeDb
        "about" -> SettingsRoutes.About
        "filter", "filter-settings" -> SettingsRoutes.FilterSettings
        "device-configuration" -> SettingsRoutes.DeviceConfiguration
        "module-configuration" -> SettingsRoutes.ModuleConfiguration
        else -> null
    }

/** Returns true when a settings [route] must be blocked unless Expert Mode is enabled. */
fun isExpertModeBlockedRoute(route: Route): Boolean = ConfigRoute.isExpertOnly(route) || ModuleRoute.isExpertOnly(route)
