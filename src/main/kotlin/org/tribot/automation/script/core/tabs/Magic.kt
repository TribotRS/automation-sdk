package org.tribot.automation.script.core.tabs

/**
 * Spellbook interaction. Spells are addressed by their in-game name (case-insensitive,
 * either an exact match or a substring fallback). Spell names can be discovered by
 * hovering the spell in the magic tab.
 *
 * For self-cast / no-target spells, [cast] performs the cast immediately. For
 * targeted spells, [cast] selects the spell and the caller is responsible for
 * clicking the target.
 */
interface Magic {

    /**
     * Casts (or selects) the spell whose name matches [name]. The match is case-
     * insensitive: an exact match wins, otherwise the first spell whose name contains
     * [name] is used.
     *
     * Automatically opens the magic tab and toggles the jewellery enchantment sub-screen
     * as needed (entering it for "Enchant" spells, leaving it for non-enchant spells).
     *
     * @return true if a matching spell was found and the click was issued.
     */
    fun cast(name: String): Boolean

    /** Whether a spell is currently selected (waiting for the player to click a target). */
    fun isSpellSelected(): Boolean

    /** The display name of the currently selected spell, or null if none is selected. */
    fun getSelectedSpellName(): String?
}
