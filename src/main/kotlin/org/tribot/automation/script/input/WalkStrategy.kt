package org.tribot.automation.script.input

/**
 * Walking surface used by [Interaction.interact] when the target is out of view and
 * the player needs to move closer.
 *
 * Once the orchestrator picks a strategy for a given call it commits to it for the
 * remainder of that call — switching surfaces mid-walk forces an expensive mouse
 * traversal between the viewport and the minimap and is the primary cause of
 * stop-and-go motion.
 */
enum class WalkStrategy {
    /**
     * Pick a strategy on the first walking iteration based on initial visibility.
     * If the furthest visible path tile is comfortably forward (a tunable threshold
     * of steps from the player) we use [SCREEN]; otherwise we use [MINIMAP]. Default.
     */
    AUTO,

    /**
     * Always click visible path tiles in the game viewport. The mouse stays in the
     * viewport region between clicks, which keeps motion smooth. Best when the
     * camera is roughly aligned with the path.
     */
    SCREEN,

    /**
     * Always click path tiles on the minimap. A single minimap click can cover ~10+
     * tiles of motion, giving us a long unbroken runway during which a camera
     * rotation can overlap. Best when the camera is misaligned or visibility is
     * limited.
     */
    MINIMAP,
}
