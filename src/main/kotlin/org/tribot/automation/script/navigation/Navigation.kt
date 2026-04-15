package org.tribot.automation.script.navigation

/**
 * Entry point for local (scene-level) reachability and pathfinding queries. Exposed via
 * [org.tribot.automation.script.ScriptContext.navigation].
 */
interface Navigation {

    /**
     * Builds a [LocalNavigation] snapshot with defaults: player as source, doors traversable,
     * no ignored tiles, no extra links.
     */
    fun snapshot(): LocalNavigation

    /** Returns a builder for configuring a [LocalNavigation] snapshot before building. */
    fun builder(): LocalNavigation.Builder
}
