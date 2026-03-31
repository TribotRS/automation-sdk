package org.tribot.automation.script.input

/**
 * Settings that control how the mouse engine generates movements.
 *
 * Accessed via [Mouse.settings]. This interface is intentionally separate from
 * [Mouse] so that the settings surface can evolve independently of the core
 * mouse operations.
 *
 * ## Movement properties
 *
 * Four behavioral properties ([MouseProperty]) shape each movement: [MouseProperty.FOCUS],
 * [MouseProperty.ACCURACY], [MouseProperty.URGENCY], and [MouseProperty.FATIGUE].
 * Each supports a three-layer control model:
 *
 * - **Base values** reflect the current task context (set internally).
 * - **Overrides** are script-set values that take priority over bases.
 * - **Multipliers** are named additive adjustments that stack.
 *
 * `effective = (override ?: base) * (1.0 + sum(multipliers))`
 *
 * ## Typical usage
 *
 * ```kotlin
 * // Temporarily increase urgency for a time-sensitive action
 * mouse.settings.setOverride(MouseProperty.URGENCY, 0.9)
 * mouse.click(x, y)
 * mouse.settings.clearOverride(MouseProperty.URGENCY)
 *
 * // Apply a script-wide focus adjustment
 * mouse.settings.setMultiplier(MouseProperty.FOCUS, "my-script", 0.15)
 * ```
 */
interface MouseSettings {

    // --- Movement property overrides ---

    /**
     * Set an override for [property]. While set, this value replaces the base
     * value entirely. Multipliers still apply on top.
     */
    fun setOverride(property: MouseProperty, value: Double)

    /** Clear the override for [property], reverting to the base value. */
    fun clearOverride(property: MouseProperty)

    // --- Movement property multipliers ---

    /**
     * Set an additive multiplier for [property]. Multipliers stack, so this value
     * will be added to the sum of all previous multipliers added in various parts of the API.
     */
    fun setMultiplier(property: MouseProperty, value: Double)

    /** Remove a named multiplier for [property]. */
    fun removeMultiplier(property: MouseProperty)

    // --- Read current values ---

    /** Get the effective value of [property] after overrides and multipliers. */
    fun getEffective(property: MouseProperty): Double

    /** Get the base value of [property] (before overrides and multipliers). */
    fun getBase(property: MouseProperty): Double

    // --- Target dimensions ---

    /**
     * Set the target dimensions used in Fitts's Law duration calculations.
     * Larger targets produce shorter estimated movement times.
     */
    fun setTargetSize(width: Double, height: Double)

    /** Reset target dimensions to defaults (50x50). */
    fun resetTargetSize()

    // --- Global mouse modifiers ---

    /** Whether pre-movement grip adjustment is enabled. Simulates grabbing the mouse after idle. */
    var gripAdjustEnabled: Boolean

    /** Whether automatic post-action drift is enabled. Simulates unconscious mouse fidgeting. */
    var postActionDriftEnabled: Boolean
}
