package org.tribot.automation.script

/**
 * Identifies a capability that a script must explicitly request before using.
 *
 * Each permission represents a behavioral boundary — not an individual setting,
 * but a threshold crossing that meaningfully changes how the bot behaves.
 * Scripts request permissions via [Permissions.request] with a human-readable
 * reason explaining why the capability is needed.
 *
 * @property description A short, user-facing explanation of what this permission enables.
 */
enum class ScriptPermission(val description: String) {
    /**
     * Allows setting mouse urgency above normal thresholds.
     * High urgency produces noticeably faster mouse movements.
     */
    HIGH_MOUSE_SPEED("Allows faster-than-normal mouse movements"),

    /**
     * Allows setting mouse focus below normal thresholds.
     * Low focus produces wandering, jittery paths with frequent overshoots.
     */
    LOW_MOUSE_FOCUS("Allows unusually unfocused mouse movements"),

    /**
     * Allows disabling the pre-movement grip adjustment simulation.
     * Grip adjust simulates the physical act of grabbing the mouse after idle.
     */
    DISABLE_GRIP_ADJUST("Allows disabling mouse grip adjustment simulation"),

    /**
     * Allows disabling automatic post-action drift.
     * Post-action drift simulates unconscious mouse fidgeting after clicks.
     */
    DISABLE_POST_ACTION_DRIFT("Allows disabling post-action mouse drift"),

    /**
     * Allows overriding target dimensions used in movement duration calculations.
     * This affects Fitts's Law estimates and can change how the mouse engine
     * plans trajectories.
     */
    CUSTOM_TARGET_SIZE("Allows overriding mouse target dimensions"),
}
