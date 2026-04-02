package org.tribot.automation.script.addon.dentistwalker

/**
 * Callback interface for controlling walk behavior.
 * Scripts implement this to react during walking (e.g., stop if target appears).
 */
fun interface WalkingCondition {

    enum class State { CONTINUE, EXIT, SUCCESS }

    fun check(): State

    companion object {
        val DEFAULT = WalkingCondition { State.CONTINUE }
    }
}
