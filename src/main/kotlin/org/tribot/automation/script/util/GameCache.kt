package org.tribot.automation.script.util

/**
 * This class provides a universal caching implementation that can cache arbitrary keys and values for:
 * - X number of game ticks
 * - X number of game cycles
 */
interface GameCache {
    fun getOrPutByTick(key: Any, load: () -> Any?): Any?

    fun getOrPutByCycle(key: Any, cacheForCycles: Int, load: () -> Any): Any
}