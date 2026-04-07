package org.tribot.automation.script.core.world

/**
 * Read-only view of a world from the OSRS world list.
 *
 * Implementations are typically backed by a snapshot from [WorldCache] or pulled
 * directly from the live client world list.
 */
interface World {
    /** The user-visible world number (e.g. 301, 302, ...). */
    val number: Int

    /** Geographic region the world is hosted in, or null if unrecognized. */
    val region: WorldRegion?

    /** Current player count reported for this world. */
    val population: Int

    /** Activity tag (e.g. "Skill total 500", "Grid Master"). May be null or "-" for none. */
    val activity: String?

    val isMembers: Boolean
    val isPvp: Boolean
    val isBounty: Boolean
    val isDeadman: Boolean
    val isFreshStartWorld: Boolean
    val isSkillTotalWorld: Boolean
    val isSpeedRunning: Boolean
    val isSeasonal: Boolean
    val isGridMaster: Boolean
}
