package org.tribot.automation.script

import net.runelite.api.Client
import org.tribot.automation.script.addon.AddonLibraries
import org.tribot.automation.script.core.*
import org.tribot.automation.script.core.definition.Definitions
import org.tribot.automation.script.input.*
import org.tribot.automation.script.client.*
import org.tribot.automation.script.navigation.Navigation
import org.tribot.automation.script.client.sidecars.BreakHandler
import org.tribot.automation.script.client.sidecars.LoginHandler
import org.tribot.automation.script.client.sidecars.Sidecars
import org.tribot.automation.script.core.tabs.Combat
import org.tribot.automation.script.core.tabs.Equipment
import org.tribot.automation.script.core.tabs.Inventory
import org.tribot.automation.script.core.tabs.Logout
import org.tribot.automation.script.core.tabs.Magic
import org.tribot.automation.script.core.tabs.Prayer
import org.tribot.automation.script.core.tabs.Skills
import org.tribot.automation.script.core.widgets.Banking
import org.tribot.automation.script.core.widgets.EnterAmount
import org.tribot.automation.script.core.widgets.Minimap
import org.tribot.automation.script.core.widgets.PinScreen
import org.tribot.automation.script.core.widgets.Tabs
import org.tribot.automation.script.core.world.WorldCache
import org.tribot.automation.script.event.Events
import org.tribot.automation.script.logging.ScriptLogger
import org.tribot.automation.script.util.Waiting

/**
 * [ScriptContext] contains the core api for interacting with the game.
 */
interface ScriptContext {
    // RuneLite API

    /**
     * A [Client] that transparently dispatches methods requiring the RuneLite client thread.
     * Safe to call from any thread.
     *
     * Return values are also wrapped when their methods are known to require the client thread
     * (e.g. the [net.runelite.api.Player] returned from [Client.getLocalPlayer], the
     * [net.runelite.api.WorldView] returned from [Client.getTopLevelWorldView], and so on
     * transitively). Anything reachable through this [Client] is safe to use from any thread.
     *
     * This is the default client to reach for. The tradeoff is allocation: every wrapped method
     * call returns a small wrapper object, adding GC pressure in tight loops over thousands of
     * game entities. Scripts that iterate huge collections per tick should use [clientRaw] with
     * explicit [clientThread] dispatch instead.
     */
    val client: Client

    /**
     * The raw RuneLite [Client] with no thread-safety wrapping. Methods that mutate or read
     * engine state must be called from the client thread (use [clientThread] to dispatch).
     *
     * Prefer [client] unless you are certain that your access pattern is on the client thread
     * or you are managing dispatch manually for performance reasons.
     */
    val clientRaw: Client

    val clientThread: org.tribot.automation.script.core.ClientThread

    // input
    val mouse: Mouse
    val keyboard: Keyboard
    val interaction: Interaction

    // definitions
    val definitions: Definitions

    // core
    val banking: Banking
    val pinScreen: PinScreen
    val enterAmount: EnterAmount
    val login: Login
    val worldCache: WorldCache
    val inventory: Inventory
    val equipment: Equipment
    val tabs: Tabs

    // game
    val skills: Skills
    val prayer: Prayer
    val combat: Combat
    val magic: Magic
    val logout: Logout
    val camera: Camera
    val minimap: Minimap

    // interaction
    val worldViews: WorldViews
    val chooseOption: ChooseOption

    // navigation
    val navigation: Navigation

    // client
    val window: Window
    val screen: Screen
    val screenPrediction: ScreenPrediction
    val runtime: Runtime
    val scripts: Scripts
    val sidecars: Sidecars
    val loginHandler: LoginHandler

    // events
    val events: Events

    // util
    val waiting: Waiting

    // permissions
    val permissions: Permissions

    // addon libraries
    val addonLibraries: AddonLibraries

    // automation (function registration & event emission)
    val automation: Automation

    // logging
    val logger: ScriptLogger
}
