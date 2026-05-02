package org.tribot.automation.script.input

/**
 * Options for the end-to-end [Interaction.interact] family of methods, which combine
 * camera adjustment and walking with the click so that an out-of-view target can be
 * driven to fully interacted-with in one call.
 */
data class InteractOptions(
    /**
     * When false, never click a walking step. The method will still rotate the camera
     * to bring the target into view, but if the target is too far for that to suffice
     * the call returns false. Use this when the caller has positioned the character
     * deliberately and does not want it to wander.
     */
    val allowMovement: Boolean = true,

    /**
     * Hard ceiling on the whole operation. After this elapses without the click
     * landing, the method returns false. Default 30 seconds.
     */
    val timeoutMs: Long = 30_000,

    /**
     * Whether to walk via viewport tile clicks, minimap clicks, or pick automatically
     * based on the initial state. The chosen strategy is sticky for the call —
     * switching surfaces mid-walk causes stop-and-go motion. See [WalkStrategy].
     */
    val walkStrategy: WalkStrategy = WalkStrategy.AUTO,
)
