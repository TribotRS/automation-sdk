package org.tribot.automation.script.core.world

/**
 * A cached view of the OSRS world list. Implementations refresh themselves
 * lazily, so callers can query repeatedly without worrying about cost or
 * doing their own download bookkeeping.
 */
interface WorldCache {
    /** All known worlds. The order of the returned list is implementation-defined. */
    fun getAll(): List<World>

    /** Looks up a world by its public world number (e.g. 301), or null if unknown. */
    fun getByNumber(worldNumber: Int): World?

    /**
     * A list of "safe to bot on" worlds: excludes pvp, deadman, beta, fresh-start,
     * skill-total, seasonal, speedrunning and grid-master worlds, plus world 401.
     *
     * @param membersOnly when true, restrict to members worlds; when false, restrict to F2P
     */
    fun getSafeWorlds(membersOnly: Boolean = true): List<World>
}
