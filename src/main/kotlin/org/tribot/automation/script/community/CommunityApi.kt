package org.tribot.automation.script.community

import org.tribot.automation.script.ScriptContext
import org.tribot.automation.script.community.antiban.randomization.Lottery
import org.tribot.automation.script.community.widgets.MakeScreen

class CommunityApi(ctx: ScriptContext) {
    val makeScreen = MakeScreen(ctx)

    /**
     * Antiban -> Randomization
     */
    val lottery = Lottery(ctx)
}