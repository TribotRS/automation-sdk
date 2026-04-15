package org.tribot.automation.script.core

import net.runelite.api.GraphicsObject
import net.runelite.api.NPC
import net.runelite.api.Player
import net.runelite.api.TileItem
import net.runelite.api.TileObject
import net.runelite.api.coords.WorldPoint

data class GroundItem(
    val item: TileItem,
    val position: WorldPoint
)

interface WorldViews {

    /**
     * Gets all players in the top level world view.
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelPlayers(): List<Player>

    /**
     * Gets all NPCs in the top level world view.
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelNpcs(): List<NPC>

    /**
     * Gets all tile objects on the current plane in the top level world view
     * (game objects, wall objects, ground objects, and decorative objects).
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelObjects(): List<TileObject>

    /**
     * Gets all ground items on the current plane in the top level world view.
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelGroundItems(): List<GroundItem>

    /**
     * Gets all graphics objects (spot animations) in the top level world view.
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelGraphicsObjects(): List<GraphicsObject>
}
