package org.tribot.automation.script.core

import net.runelite.api.coords.LocalPoint
import java.awt.Point

/**
 * Predicts where points will appear on screen after a given duration, accounting for
 * viewport and minimap shifts caused by player movement.
 *
 * These predictions are only meaningful while the local player is moving. When stationary,
 * both methods return null.
 *
 * Predictions use a two-phase movement model:
 * 1. Visual catch-up: the player's rendered position interpolating toward the server position
 * 2. Destination movement: continued movement toward the pathfinding destination at walk/run speed
 *
 * A lead factor (0.0-1.0) controls how aggressively the prediction leads the movement.
 * Lower values are more conservative, higher values compensate more fully.
 */
interface ScreenPrediction {

    /**
     * Predicts where a local point will appear on the game viewport after [durationMs],
     * accounting for camera shift caused by player movement.
     *
     * @param targetLocal the target's local coordinates
     * @param targetPlane the map plane (z-level) of the target
     * @param targetHeight the height offset above the tile (e.g., half the model's logical height)
     * @param durationMs how far into the future to predict, in milliseconds
     * @param leadFactor how aggressively to lead the prediction (0.0-1.0, default 0.65)
     * @return the predicted screen point, or null if the player is not moving or the point leaves the viewport
     */
    fun predictViewportPoint(
        targetLocal: LocalPoint,
        targetPlane: Int,
        targetHeight: Int,
        durationMs: Double,
        leadFactor: Double = 0.65,
    ): Point?

    /**
     * Predicts where a local point will appear on the minimap after [durationMs],
     * accounting for the minimap shifting as the player moves.
     *
     * @param targetLocal the target's local coordinates
     * @param durationMs how far into the future to predict, in milliseconds
     * @param leadFactor how aggressively to lead the prediction (0.0-1.0, default 0.5)
     * @return the predicted minimap point, or null if the player is not moving or the point leaves minimap range
     */
    fun predictMinimapPoint(
        targetLocal: LocalPoint,
        durationMs: Double,
        leadFactor: Double = 0.5,
    ): Point?
}
