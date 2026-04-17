package org.tribot.automation.script.core

import net.runelite.api.GraphicsObject
import net.runelite.api.NPC
import net.runelite.api.Player
import net.runelite.api.Tile
import net.runelite.api.TileItem
import net.runelite.api.TileObject
import net.runelite.api.WorldView
import net.runelite.api.coords.WorldPoint

data class GroundItem(
    val item: TileItem,
    val position: WorldPoint
) {
    companion object {
        /**
         * Wraps every [TileItem] on [tile] as a [GroundItem], pairing each with the tile's
         * [WorldPoint]. Returns an empty list if the tile has no ground items.
         *
         * Intended for use inside [WorldViews.withTiles] blocks, where tiles are already
         * being iterated on the client thread. Must be called on the client thread, since
         * it reads live scene data.
         */
        fun fromTile(tile: Tile): List<GroundItem> {
            val items = tile.groundItems ?: return emptyList()
            val position = tile.worldLocation
            return items.map { GroundItem(it, position) }
        }
    }
}

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
     * Iterates the scene's tiles in closest-first order from [source], yielding them to [block]
     * as a lazy [Sequence]. Everything inside [block] (including consumption of the sequence)
     * runs on the client thread. The value returned from [block] is returned from this function.
     *
     * ## Why this shape
     *
     * Tile objects and ground items are stored per-tile, not in flat lists. Most callers want
     * "the closest X" and can short-circuit once found. Running the iteration as a single
     * client-thread call (rather than per-element) avoids per-tile thread hops, while exposing
     * the tiles as a [Sequence] lets callers use `firstOrNull`, `takeWhile`, `filter`, etc.
     * naturally and bail out as soon as they have what they need.
     *
     * ## Iteration order
     *
     * Closest-first using **Chebyshev distance** (square rings expanding outward from [source]).
     * Distance 1 means any of the 8 surrounding tiles, distance 2 means any tile in the 5x5 box
     * excluding the 3x3 center, and so on. Tiles at the same Chebyshev distance have no
     * guaranteed ordering relative to each other. If you need a different metric (Euclidean,
     * pathing, etc.), pull the first N tiles and sort them yourself.
     *
     * ## Parameters
     *
     * @param worldView The world view to iterate. `null` (the default) uses the top-level world
     *   view. Pass a specific [WorldView] to iterate an instance (POH, raids, etc.).
     * @param source The origin to iterate outward from. `null` (the default) uses the local
     *   player's current scene location. If non-null and the point is not loaded in [worldView]'s
     *   scene, the sequence will be empty.
     * @param maxDistance Maximum Chebyshev distance (in tiles) to iterate. Defaults to 104, which
     *   effectively covers the whole scene regardless of origin. Must be non-negative.
     * @param block Invoked exactly once on the client thread with the lazy [Sequence] of tiles.
     *   Tiles on only the [source]'s plane are yielded. The block's return value becomes this
     *   function's return value.
     *
     * ## Constraints on the sequence
     *
     * The provided [Sequence] is valid **only** for the duration of the [block] invocation:
     *
     * - It iterates live scene data and must be consumed on the client thread (handled for you
     *   since [block] already runs there).
     * - Consuming it after [block] returns throws [IllegalStateException]. Don't stash the
     *   sequence in a `var` for later use; derive what you need inside the block and return it.
     * - It is single-pass; iterate at most once.
     *
     * ## Example
     *
     * ```kotlin
     * val bankObjectIds = setOf(1234, 5678)
     * val nearestBank = worldViews.withTiles(maxDistance = 20) { tiles ->
     *     tiles
     *         .flatMap { it.gameObjects?.asSequence()?.filterNotNull() ?: emptySequence() }
     *         .firstOrNull { it.id in bankObjectIds }
     * }
     * ```
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     *
     * @throws IllegalStateException if no world view is available, if [source] is null and there
     *   is no local player, or if the sequence is consumed after [block] returns.
     */
    fun <R> withTiles(
        worldView: WorldView? = null,
        source: WorldPoint? = null,
        maxDistance: Int = 104,
        block: (Sequence<Tile>) -> R?
    ): R?

    /**
     * Resolves the [Tile] at [point] in [worldView]. Returns null if the point is not loaded
     * in the world view's scene (out-of-bounds plane, unloaded chunk, etc.).
     *
     * @param point The world coordinate to resolve.
     * @param worldView The world view to look up in. `null` (the default) uses the top-level
     *   world view. Pass a specific [WorldView] for an instance (POH, raids, etc.).
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun tileAt(point: WorldPoint, worldView: WorldView? = null): Tile?

    /**
     * Gets all graphics objects (spot animations) in the top level world view.
     *
     * Runs on the client thread. Executes synchronously if already on the client thread.
     */
    fun getTopLevelGraphicsObjects(): List<GraphicsObject>



    @Deprecated("Use withTiles instead")
    fun getTopLevelObjects(): List<TileObject>
    @Deprecated("Use withTiles instead")
    fun getTopLevelGroundItems(): List<GroundItem>
}
