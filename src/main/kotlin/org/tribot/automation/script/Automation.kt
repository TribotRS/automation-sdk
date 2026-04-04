package org.tribot.automation.script

import org.tribot.automation.GameFunction

/**
 * Provides scripts with the ability to register functions (exposed via JSON-RPC)
 * and emit events (sent as JSON-RPC notifications to the launcher).
 *
 * Functions registered through this interface are automatically cleaned up
 * when the script ends.
 */
interface Automation {

    /**
     * Registers a function that will be callable via the launcher's JSON-RPC API.
     * If a function with the same name already exists, it is replaced.
     */
    fun registerFunction(name: String, function: GameFunction)

    /**
     * Unregisters a previously registered function.
     */
    fun unregisterFunction(name: String)

    /**
     * Emits an event as a JSON-RPC notification to the launcher.
     *
     * @param name the event name/type
     * @param event the event payload (JSON string)
     */
    fun emitEvent(name: String, event: String)
}
