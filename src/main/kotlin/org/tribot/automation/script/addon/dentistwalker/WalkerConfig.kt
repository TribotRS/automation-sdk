package org.tribot.automation.script.addon.dentistwalker

data class WalkerConfig(
    val apiKey: String = "",
    val baseUrl: String = "https://dentist-scripts.com",
    val timeoutMs: Long = 10_000,

    // Path randomization
    val randomize: Boolean = true,

    // Profile feature toggles
    val profileEnabled: Boolean = true,
    val tileOffsetEnabled: Boolean = true,
    val clickTimingEnabled: Boolean = true,
    val cameraAlignEnabled: Boolean = true,
    val idleJiggleEnabled: Boolean = true,
    val prehoverEnabled: Boolean = true,
    val multiClickEnabled: Boolean = true,
    val burstClickEnabled: Boolean = true,
    val screenWalkEnabled: Boolean = true,
    val mouseOffscreenEnabled: Boolean = true,

    // Logging
    val logging: Boolean = true,
    val debugLogging: Boolean = false,
)
