package org.tribot.automation.script.input

import net.runelite.api.Actor
import net.runelite.api.TileObject
import net.runelite.api.coords.WorldPoint
import net.runelite.api.widgets.Widget
import org.tribot.automation.script.core.GroundItem

interface Interaction {

    // -- Actors (NPC, Player) --

    fun click(actor: Actor, option: String): Boolean
    fun hover(actor: Actor): Boolean

    // -- Objects --

    fun click(obj: TileObject, option: String): Boolean
    fun hover(obj: TileObject): Boolean

    // -- End-to-end interaction --
    //
    // Like [click], but if the target is out of view the implementation will rotate
    // the camera and (when [InteractOptions.allowMovement] is true) walk towards the
    // target until it is visible, then click it. The intent is one call that handles
    // every case a script might encounter, with smooth, non-robotic motion.

    /**
     * Drives the character to interact with [actor] using [option], handling camera
     * and walking automatically when the actor is out of view. Returns true if the
     * action was sent. See [InteractOptions] for tuning.
     */
    fun interact(actor: Actor, option: String, options: InteractOptions = InteractOptions()): Boolean

    /**
     * Drives the character to interact with [obj] using [option], handling camera
     * and walking automatically when the object is out of view. Returns true if the
     * action was sent.
     */
    fun interact(obj: TileObject, option: String, options: InteractOptions = InteractOptions()): Boolean

    /**
     * Drives the character to interact with the ground item identified by [itemId]
     * at [position] using [option]. Mirrors [clickGroundItem] but adds camera and
     * walking handling.
     */
    fun interactGroundItem(
        itemId: Int,
        position: WorldPoint,
        option: String,
        options: InteractOptions = InteractOptions(),
    ): Boolean

    fun interact(groundItem: GroundItem, option: String, options: InteractOptions = InteractOptions()): Boolean =
        interactGroundItem(groundItem.item.id, groundItem.position, option, options)

    // -- Ground items --

    /**
     * Clicks the ground item of id [itemId] on the tile at [position]. The pair uniquely
     * identifies the intended [net.runelite.api.MenuEntry] even when multiple items of the
     * same id sit on different tiles.
     *
     * This raw form is for callers who hold an id and a position but no [GroundItem]
     * (e.g. loaded from config). If you already have a [GroundItem], prefer the
     * [click] overload that takes one.
     */
    fun clickGroundItem(itemId: Int, position: WorldPoint, option: String): Boolean

    /**
     * Moves the cursor onto the ground-item stack at [position]. Ground items on the same
     * tile share a clickbox, so hovering is a tile-level operation: it cannot target one
     * item in a stack over another. Returns false if the tile is not loaded or carries
     * no ground items.
     */
    fun hoverItemLayer(position: WorldPoint): Boolean

    fun click(groundItem: GroundItem, option: String): Boolean =
        clickGroundItem(groundItem.item.id, groundItem.position, option)

    fun hover(groundItem: GroundItem): Boolean =
        hoverItemLayer(groundItem.position)

    // -- Tiles --

    /**
     * Left-clicks the tile at [position] and verifies the menu action was `Walk here`.
     * This is a single-tile walk: no pathing is performed. For multi-tile navigation
     * use a higher-level walker that feeds individual tiles to this method.
     *
     * Unlike object/actor clicks, there is only one meaningful action on a bare tile
     * (walking), so no option parameter is taken. If something on the tile steals the
     * click (an NPC, a ground item), the underlying retry logic reselects a new random
     * point on the tile before falling back to the right-click menu.
     *
     * @param avoidGroundItems When true, the random click point is biased to fall
     *   outside the tile's ground-item clickbox when possible, so the default left-click
     *   action remains `Walk here` instead of the ground-item action. Best-effort: if
     *   the item stack covers the entire visible tile, a point inside it may still be
     *   chosen rather than failing outright. Has no effect on tiles without ground items.
     */
    fun walkHere(position: WorldPoint, avoidGroundItems: Boolean = false): Boolean

    /**
     * Moves the cursor onto the tile at [position] using the same point-selection logic
     * as [walkHere], but does not click. Returns false if the tile isn't visible.
     *
     * @param avoidGroundItems When true, the cursor is biased to land outside the tile's
     *   ground-item clickbox when possible, matching [walkHere]. Useful when pre-hovering
     *   a tile that will be walked to shortly — landing outside the item layer keeps
     *   `Walk here` as the default left-click action. Best-effort; see [walkHere].
     */
    fun hoverTile(position: WorldPoint, avoidGroundItems: Boolean = false): Boolean

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
    fun scrollChildIntoView(parent: Widget, child: Widget): Boolean

    /**
     * Returns true if [child] is not fully contained within [parent]'s vertical
     * extent and would therefore require a scroll to become visible.
     */
    fun needsScroll(parent: Widget, child: Widget): Boolean
}
