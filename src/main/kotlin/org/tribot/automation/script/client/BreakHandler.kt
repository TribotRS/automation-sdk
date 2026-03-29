package org.tribot.automation.script.client

interface BreakHandler {
    val isOnBreak: Boolean
    val millisUntilNextBreak: Long?
    val millisUntilBreakEnd: Long?
}
