package org.tribot.automation

import org.tribot.automation.script.ScriptContext

interface TribotScript {
    fun execute(context: ScriptContext)
}
