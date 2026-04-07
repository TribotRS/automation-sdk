package org.tribot.automation.script.core.widgets

/**
 * Bank pin entry screen and the related "pending pin" / confirmation dialogs.
 */
interface PinScreen {
    /** True if the bank pin entry screen is currently open. */
    fun isOpen(): Boolean

    /** True if a bank pin confirmation/message dialog is showing (e.g. "incorrect pin"). */
    fun isMessageOpen(): Boolean

    /** True if the "pending pin" waiting-period screen is showing. */
    fun isPendingPinOpen(): Boolean

    /** True if a bank pin has been configured for the active account. */
    fun isBankPinSet(): Boolean

    /** Enters the bank pin from the active account's configured data. */
    fun enterPin(): Boolean

    /**
     * Enters the supplied pin into the bank pin screen, padding shorter pins with leading zeros.
     */
    fun enterPin(pin: String): Boolean

    /** Closes the bank pin confirmation/message dialog if open. Returns true if it ended up closed. */
    fun closeMessage(): Boolean

    /** Cancels/closes the pending pin (waiting period) screen if open. */
    fun closePendingPin(): Boolean
}
