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

    /**
     * Scrolls [child] into view inside [parent] using the mouse wheel.
     *
     * If [child] is already within [parent]'s vertical bounds, returns true without
     * scrolling. Returns false if either widget (or its bounds) is null, or if the
     * scroll did not land the child in view before the timeout.
     *
     * The mouse will first be moved into [parent]'s bounds if it is not already
     * there, since wheel events are delivered to whatever widget is under the cursor.
     */
    fun scrollChildIntoView(parent: Widget?, child: Widget?): Boolean

    /**
     * Returns true if [child] is not fully contained within [parent]'s vertical
     * extent and would therefore require a scroll to become visible.
     */
    fun needsScroll(parent: Widget?, child: Widget?): Boolean
}
