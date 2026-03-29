package org.tribot.automation.script.core.widgets

import net.runelite.api.coords.WorldPoint

interface Minimap {

    /**
     * Clicks a tile on the minimap.
     *
     * @return false if the tile is not on the minimap
     */
    fun clickTile(point: WorldPoint): Boolean
}