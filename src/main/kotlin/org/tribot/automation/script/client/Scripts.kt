package org.tribot.automation.script.client

interface Scripts {
    fun getAvailable(): List<ScriptInfo>

    fun run(script: ScriptInfo, args: String = ""): ScriptHandle?
}

interface ScriptInfo {
    val name: String
}

interface ScriptHandle {
    val name: String
    val isPaused: Boolean
    val isRunning: Boolean

    fun pause()
    fun resume()
    fun stop()
    fun waitFor()
    fun waitFor(timeoutMs: Long): Boolean
}
