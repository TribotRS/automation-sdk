package org.tribot.automation.script.core

import net.runelite.api.Actor
import net.runelite.api.coords.WorldPoint

enum class CameraMethod {
    KEYS,
    MOUSE
}

interface Camera {
    fun getAngle(): Int
    fun getRotation(): Int
    fun getZoom(): Double
    fun setAngle(angle: Int, method: CameraMethod)
    fun setRotation(rotation: Int, method: CameraMethod)
    fun turnTo(point: WorldPoint, method: CameraMethod)
    fun turnTo(actor: Actor, method: CameraMethod)
    fun setCamera(angle: Int, rotation: Int, method: CameraMethod)

}
