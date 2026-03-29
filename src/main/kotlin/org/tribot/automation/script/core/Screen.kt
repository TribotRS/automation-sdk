package org.tribot.automation.script.core

import java.awt.Point
import java.awt.Rectangle

interface Screen {
    fun getViewportBounds(): Rectangle

    fun isPointInViewport(point: Point): Boolean
}