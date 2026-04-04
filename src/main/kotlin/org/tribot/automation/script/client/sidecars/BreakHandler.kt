package org.tribot.automation.script.client.sidecars

interface BreakHandler: ScriptSidecar {
    val isOnBreak: Boolean
    val millisUntilNextBreak: Long?
    val millisUntilBreakEnd: Long?
}