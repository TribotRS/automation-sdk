package org.tribot.automation

import net.runelite.api.Client

typealias EventEmitter = (name: String, event: String) -> Unit
typealias GameFunction = (input: String?) -> String
typealias GameListener = () -> Unit

class TribotPluginContext(
    val automationApi: TribotAutomationApi,
    val eventEmitter: EventEmitter,
    val client: Client,
    val logger: TribotPluginLogger,
)

interface TribotPlugin {
    fun start(context: TribotPluginContext)
    fun stop()
}

/**
 * Logger provided to plugins for writing to the Tribot GUI log panel.
 */
interface TribotPluginLogger {
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
    fun debug(message: String)
}

interface TribotAutomationApi {
    fun registerFunction(name: String, function: GameFunction)
    fun unregisterFunction(name: String)

    fun registerTickListener(listener: GameListener)
    fun unregisterTickListener(listener: GameListener)

    fun getScripts(): List<ScriptData>

    /**
     * Starts a script. Returns a [ScriptStartResult] indicating success (with a run ID)
     * or failure (with a reason string).
     *
     * Only one script can run at a time across all plugins. Concurrent calls are
     * serialized via an internal lock.
     */
    fun runScript(scriptData: ScriptData, args: String = ""): ScriptStartResult

    /**
     * Returns information about the currently running script, or null if no script is running.
     */
    fun getRunningScript(): RunningScriptInfo?

    fun stopScript()
    fun pauseScript()
    fun resumeScript()
}

/**
 * Result of attempting to start a script via [TribotAutomationApi.runScript].
 */
sealed interface ScriptStartResult {
    /** Script started successfully. */
    data class Success(val runId: String) : ScriptStartResult

    /** Script failed to start. */
    data class Error(val reason: String) : ScriptStartResult
}

interface ScriptData {
    val name: String
    val isLocal: Boolean
    val version: String
}

/**
 * Snapshot of the currently running script's state.
 */
interface RunningScriptInfo {
    /** Run ID assigned when the script was started via [TribotAutomationApi.runScript], or null if started externally (GUI/auto-launch). */
    val runId: String?
    val name: String
    val isPaused: Boolean
}
