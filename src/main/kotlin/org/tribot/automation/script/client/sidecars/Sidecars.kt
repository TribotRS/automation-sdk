package org.tribot.automation.script.client.sidecars

interface ScriptSidecar {
    val name: String
    val isActive: Boolean

    var isEnabled: Boolean
}

interface Sidecars {
    val loginHandler: LoginHandler
    val breakHandler: BreakHandler

    fun getAll(): List<ScriptSidecar>
}
