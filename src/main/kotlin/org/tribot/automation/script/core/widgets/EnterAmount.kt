package org.tribot.automation.script.core.widgets

/**
 * The "Enter amount" chatbox prompt that appears when a withdraw/deposit/use-X
 * action requests a numeric input.
 */
interface EnterAmount {
    /** True if the "Enter amount" prompt is currently open. */
    fun isOpen(): Boolean

    /**
     * Types the supplied amount and submits the prompt.
     *
     * Returns true if the prompt closed within the expected timeout.
     */
    fun enter(amount: Int): Boolean
}
