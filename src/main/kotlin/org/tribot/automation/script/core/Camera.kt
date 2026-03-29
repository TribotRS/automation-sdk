package org.tribot.automation.script.core

import net.runelite.api.Actor
import net.runelite.api.coords.WorldPoint

interface Camera {
    fun getAngle(): Int
    fun getRotation(): Int
    fun getZoom(): Double
    fun setAngle(angle: Int)
    fun setRotation(rotation: Int)
    fun turnTo(point: WorldPoint)
    fun turnTo(actor: Actor)
}