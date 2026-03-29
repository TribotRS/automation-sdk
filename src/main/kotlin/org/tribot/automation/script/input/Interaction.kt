package org.tribot.automation.script.input

import net.runelite.api.Actor
import net.runelite.api.TileObject
import net.runelite.api.widgets.Widget

interface Interaction {

    // -- Actors (NPC, Player) --

    fun click(actor: Actor, option: String): Boolean
    fun hover(actor: Actor): Boolean

    // -- Objects --

    fun click(obj: TileObject, option: String): Boolean
    fun hover(obj: TileObject): Boolean

    // -- Widgets --

    fun click(widget: Widget): Boolean
    fun click(widget: Widget, option: String): Boolean
    fun hover(widget: Widget): Boolean
}
