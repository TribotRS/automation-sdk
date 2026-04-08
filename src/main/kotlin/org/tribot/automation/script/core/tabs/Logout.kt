package org.tribot.automation.script.core.tabs

/**
 * The logout tab. Performs the in-game logout sequence: opens the tab and clicks the
 * "Logout" button. For pre-game login flow (entering credentials, handling auth
 * prompts, world hopping) see [org.tribot.automation.script.core.Login].
 */
interface Logout {
    /**
     * Logs the player out to the login screen. Returns true if the client ended up
     * on the login screen, including the case where it was already there.
     */
    fun logout(): Boolean
}
