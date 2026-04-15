package org.tribot.automation.script.navigation

import net.runelite.api.coords.WorldPoint

/**
 * An immutable, query-only snapshot of the local 104x104 scene's walk graph with pre-computed
 * BFS distances from a source tile. Queries do not require the client thread.
 *
 * The snapshot is faithful only while the scene's base coordinates and plane match those
 * captured at build time; after a loading zone the instance becomes stale and a new snapshot
 * should be built via [Navigation].
 */
interface LocalNavigation {

    /** Returns true if [target] is reachable from the snapshot source. */
    fun canReach(target: WorldPoint): Boolean

    /** Returns the BFS path distance in tiles (Chebyshev steps) to [target], or -1 if unreachable. */
    fun distanceTo(target: WorldPoint): Int

    /**
     * Returns the reconstructed path from the snapshot source to [target], inclusive of both
     * endpoints, or null if unreachable. The first element is the source tile, the last is [target].
     */
    fun pathTo(target: WorldPoint): List<WorldPoint>?

    /** Returns the candidate with the smallest reachable path distance, or null if none are reachable. */
    fun closestReachable(candidates: Iterable<WorldPoint>): WorldPoint?

    /** Returns the reachable subset of [candidates], sorted by path distance ascending. */
    fun sortedByPathDistance(candidates: Iterable<WorldPoint>): List<WorldPoint>

    /**
     * Configures and builds a [LocalNavigation] snapshot. Obtain via [Navigation.builder].
     *
     * Once [build] is called the snapshot is frozen; to apply changes (player moved, scene
     * loaded, etc.) create a new builder and rebuild.
     */
    interface Builder {
        /** Sets the source tile for BFS. Defaults to the local player's current tile. */
        fun source(point: WorldPoint): Builder

        /**
         * If true (default), doors ("Open"/"Close" wall objects) act as open edges between
         * their adjacent cardinal tiles — useful for questing / walking assumptions where
         * the script intends to open doors it encounters.
         */
        fun travelThroughDoors(enabled: Boolean): Builder

        /** Tiles BFS should treat as blocked destinations. Off-plane or off-scene entries are dropped. */
        fun ignore(tiles: Set<WorldPoint>): Builder

        /**
         * Extra directed adjacencies (e.g. agility shortcuts, teleporters). For bidirectional
         * links, supply both directions. Link edges bypass wall and door checks.
         */
        fun linkTiles(links: Map<WorldPoint, Set<WorldPoint>>): Builder

        fun build(): LocalNavigation
    }
}
