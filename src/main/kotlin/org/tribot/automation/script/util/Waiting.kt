package org.tribot.automation.script.util

interface Waiting {
    /**
     * Sleeps the current thread for the specified number of milliseconds and allows the script
     * to pause execution if needed (user pauses the script, login bot needs to activate, break activates, etc).
     *
     * Prefer this over Thread.sleep for any script code.
     *
     * @param ms The number of milliseconds to sleep.
     */
    fun sleep(ms: Long)
}
