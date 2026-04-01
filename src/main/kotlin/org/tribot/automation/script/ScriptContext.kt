package org.tribot.automation.script

import net.runelite.api.Client
import net.runelite.client.callback.ClientThread
import org.tribot.automation.script.addon.AddonLibraries
import org.tribot.automation.script.core.*
import org.tribot.automation.script.core.definition.Definitions
import org.tribot.automation.script.input.*
import org.tribot.automation.script.client.*
import org.tribot.automation.script.core.tabs.Combat
import org.tribot.automation.script.core.tabs.Equipment
import org.tribot.automation.script.core.tabs.Inventory
import org.tribot.automation.script.core.tabs.Prayer
import org.tribot.automation.script.core.tabs.Skills
import org.tribot.automation.script.core.widgets.Banking
import org.tribot.automation.script.core.widgets.Minimap
import org.tribot.automation.script.core.widgets.Tabs
import org.tribot.automation.script.event.Events
import org.tribot.automation.script.util.Waiting

/**
 * [ScriptContext] contains the core api for interacting with the game.
 */
interface ScriptContext {
    // RuneLite API
    val client: Client
    val clientThread: ClientThread

    // input
    val mouse: Mouse
    val keyboard: Keyboard
    val interaction: Interaction

    // definitions
    val definitions: Definitions

    // core
    val banking: Banking
    val loginScreen: LoginScreen
    val inventory: Inventory
    val equipment: Equipment
    val tabs: Tabs

    // game
    val skills: Skills
    val prayer: Prayer
    val combat: Combat
    val camera: Camera
    val minimap: Minimap

    // interaction
    val worldViews: WorldViews
    val chooseOption: ChooseOption

    // client
    val window: Window
    val screen: Screen
    val screenPrediction: ScreenPrediction
    val runtime: Runtime
    val scripts: Scripts
    val breakHandler: BreakHandler

    // events
    val events: Events

    // util
    val waiting: Waiting

    // permissions
    val permissions: Permissions

    // addon libraries
    val addonLibraries: AddonLibraries
}
