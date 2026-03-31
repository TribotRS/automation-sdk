package org.tribot.automation.script.input

/**
 * Identifies a behavioral property of mouse movement that can be customized
 * via [MouseSettings].
 *
 * Each property supports three layers of control:
 * - **Base value**: the default set by internal systems for the current task context.
 * - **Override**: a script-set value that takes priority over the base.
 * - **Multipliers**: additive adjustments that stack on top.
 *
 * Effective value = `(override ?: base) * (1.0 + sum(multipliers))`
 */
enum class MouseProperty {
    /**
     * Overall movement quality. High focus produces clean, direct, steady paths
     * with minimal jitter and few overshoots. Low focus produces wandering, drifty
     * paths with more jitter and occasional overshoots.
     *
     * Range: 0.0 (zoned out) to 1.0 (fully focused). Default: 0.55.
     */
    FOCUS,

    /**
     * How precisely the initial ballistic phase aims at the target. High accuracy
     * means the mouse launches directly toward the target with small corrections.
     * Low accuracy means the mouse aims poorly at first, then corrects.
     *
     * Range: 0.0 (rough area click) to 1.0 (pixel-perfect). Default: 0.35.
     */
    ACCURACY,

    /**
     * Speed dial. High urgency produces fast movements (up to 40% faster).
     * Note: mouse speed should not generally go above 1.5 for typical human input. The faster the movement speed, the less
     * data gets sent for curves and such to function properly. Only use extreme speeds when it's genuinely the only option.
     *
     * Range: 0.0 (no rush) to 2.0. Default: 0.70 - but changes frequently up to 0.85 for various tasks.
     */
    URGENCY,

    /**
     * Long-session degradation. Movements get slower, noisier, and less precise
     * as fatigue increases.
     *
     * Range: 0.0 (fresh) to 1.0 (exhausted). Default: 0.0.
     */
    FATIGUE,
}
