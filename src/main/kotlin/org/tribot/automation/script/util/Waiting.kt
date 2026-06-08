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

    /**
     * Sleeps the current thread until the specified condition is true, checking on each frame (client side tick). Useful
     * for clientside conditions like waiting for a menu/interface to appear.
     *
     * @return true if the condition was met, false if maxFrames elapsed first.
     */
    fun sleepFramesUntil(maxFrames: Int, condition: () -> Boolean): Boolean

    /**
     * Sleeps the current thread until the specified condition is true, checking on each tick (server side tick). Useful
     * for serverside conditions like waiting for a game action to complete.
     *
     * @return true if the condition was met, false if maxTicks elapsed first.
     */
    fun sleepTicksUntil(maxTicks: Int, condition: () -> Boolean): Boolean
}
