package org.tribot.automation.script.core

import java.awt.Point
import java.awt.Rectangle

interface Screen {
    /**
     * Gets the canvas dimension, including HUD and minimap.
     */
    fun getCanvasDimensions(): Rectangle

    /**
     * Checks if a given point is within the viewport, calculated as any area of the screen that isn't covered by the
     * "fixed" HUD widgets (chat, tabs, minimap, etc.). Works in both fixed and resizable modes.
     *
     * This method uses pre-calculated HUD dimensions for performance, which means it does NOT work with any runelite plugins
     * that change the HUD dimensions/locations.
     */
    fun isPointInViewport(point: Point): Boolean
}