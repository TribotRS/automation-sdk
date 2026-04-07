package org.tribot.automation.script.core.world

/**
 * Geographic region a world is hosted in. Maps to the raw `location` flag in the
 * world list payload.
 */
enum class WorldRegion(val alpha2: String, val alpha3: String) {
    UNITED_STATES_OF_AMERICA("US", "USA"),
    UNITED_KINGDOM("GB", "GBR"),
    AUSTRALIA("AU", "AUS"),
    GERMANY("DE", "DEU");

    companion object {
        /** Resolves a region from the raw location flag, or null if unknown. */
        fun fromLocation(location: Int): WorldRegion? = when (location) {
            0 -> UNITED_STATES_OF_AMERICA
            1 -> UNITED_KINGDOM
            3 -> AUSTRALIA
            7 -> GERMANY
            else -> null
        }
    }
}
