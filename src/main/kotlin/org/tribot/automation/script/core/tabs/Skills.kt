package org.tribot.automation.script.core.tabs

import net.runelite.api.Skill

/**
 * Read-only access to the player's skill levels and experience, plus interaction with
 * the skill tab itself. All level-returning methods use the standard OSRS scale (1-99
 * for normal skills, with virtual levels above 99 supported by [getLevelForXp]).
 */
interface Skills {

    // --- Live skill state ---

    /** Returns the player's actual (un-boosted) level in [skill]. */
    fun getLevel(skill: Skill): Int

    /** Returns the player's current (boosted or drained) level in [skill]. */
    fun getBoostedLevel(skill: Skill): Int

    /** Returns the player's total experience in [skill]. */
    fun getXp(skill: Skill): Int

    /** Returns the experience required to reach the next level. 0 if already at max level. */
    fun getXpToNextLevel(skill: Skill): Int

    /**
     * Returns the experience required to reach [targetLevel]. 0 if the player is already
     * at max level.
     */
    fun getXpToLevel(skill: Skill, targetLevel: Int): Int

    /** Returns the percentage (0-100) of progress toward the next level. */
    fun getPercentToNextLevel(skill: Skill): Int

    /** Returns the percentage (0-100) of progress from the current level toward [targetLevel]. */
    fun getPercentToLevel(skill: Skill, targetLevel: Int): Int

    // --- XP table lookups (pure functions of the OSRS XP curve) ---

    /**
     * Returns the cumulative xp required to reach the given [level]. Valid levels are
     * 1-127 (the in-game virtual level cap). Returns -1 for out-of-range levels.
     */
    fun getXpForLevel(level: Int): Int

    /**
     * Returns the level reached at the given [xp] amount. Returns -1 for negative xp,
     * and is capped at 99 (use lookups against [getXpForLevel] for virtual levels).
     */
    fun getLevelForXp(xp: Int): Int

    // --- Interaction ---

    /**
     * Hovers the mouse over [skill] in the skills tab, opening the tab if needed. Useful
     * for revealing the skill's tooltip.
     */
    fun hover(skill: Skill): Boolean

    /**
     * Clicks [skill] in the skills tab, opening the tab if needed.
     */
    fun click(skill: Skill): Boolean
}
