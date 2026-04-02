package org.tribot.automation.script.addon.dentistwalker

import net.runelite.api.coords.WorldPoint

/**
 * DentistWalker — web-walking API for automation-sdk scripts.
 *
 * Access via `ctx.addonLibraries.dentistWalker`.
 */
interface DentistWalker {

    // ---- Configuration ----

    fun configure(config: WalkerConfig)

    // ---- Walk to destination ----

    fun walkTo(destination: WorldPoint, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    fun walkTo(x: Int, y: Int, z: Int = 0, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    // ---- Walk to bank ----

    fun walkToBank(condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    fun walkToBank(bank: Banks, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    // ---- Sailing ----

    fun sailTo(destination: WorldPoint, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    fun sailTo(x: Int, y: Int, z: Int = 0, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean

    // ---- Local walking (no server call) ----

    fun localWalk(path: List<WorldPoint>, condition: WalkingCondition = WalkingCondition.DEFAULT): Boolean
}
