package org.tribot.automation.script.client

import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

interface Window {
    fun getPosition(): Point
    fun setPosition(x: Int, y: Int)

    fun getSize(): Dimension
    fun setSize(width: Int, height: Int)

    fun minimize()
    fun maximize()
    fun restore()
}
