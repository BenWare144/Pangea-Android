/*
 * Copyright (c) 2026 Meshtastic LLC
 */
package org.meshtastic.feature.settings.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.meshtastic.core.navigation.SettingsRoutes

class ExpertModeRouteGuardTest {

    @Test
    fun `settings route mapping supports lora slug`() {
        assertEquals(SettingsRoutes.LoRa, settingsRouteFromDeepLinkSegment("lora"))
    }

    @Test
    fun `settings route mapping supports external notification aliases`() {
        assertEquals(SettingsRoutes.ExtNotification, settingsRouteFromDeepLinkSegment("external-notification"))
        assertEquals(SettingsRoutes.ExtNotification, settingsRouteFromDeepLinkSegment("extnotification"))
    }

    @Test
    fun `expert guard blocks lora and mqtt routes`() {
        assertTrue(isExpertModeBlockedRoute(SettingsRoutes.LoRa))
        assertTrue(isExpertModeBlockedRoute(SettingsRoutes.MQTT))
    }

    @Test
    fun `expert guard does not block user route`() {
        assertFalse(isExpertModeBlockedRoute(SettingsRoutes.User))
    }
}
