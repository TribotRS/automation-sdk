package org.tribot.automation.script.client

interface Runtime {
    val scriptName: String
    val scriptArgs: String
    val isLocal: Boolean
    val runTimeMs: Long

    fun getOsrsAccountHash(): String?
    fun getTribotUsername(): String?
}
