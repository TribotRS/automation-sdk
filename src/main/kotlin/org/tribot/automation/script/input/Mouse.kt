package org.tribot.automation.script.input

import java.awt.Point
import java.awt.Rectangle
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

interface Mouse {

    /** Settings that control how the mouse engine generates movements. */
    val settings: MouseSettings

    /** Current mouse position on the game canvas. */
    val position: Point

    // -- Movement --

    /** Move the mouse to [x], [y] using the active mouse engine. */
    fun move(x: Int, y: Int)

    /**
     * Move to a live-blending target reference (mousev2 dynamic clicking).
     *
     * The caller continuously updates [target] while the movement is in flight;
     * the engine blends small deltas into the trajectory on each step.
     * Set [done] to `true` when the target is finalized so the engine can land.
     */
    fun move(target: AtomicReference<Point>, done: AtomicBoolean)

    /** Instantly teleport the cursor to [x], [y] without any movement animation. */
    fun hop(x: Int, y: Int)

    /**
     * Estimates how long the mouse will take to travel a given pixel distance
     * to a target of the given dimensions, using the current account profile
     * and movement context (focus, urgency, fatigue).
     */
    fun estimateMouseTravelTime(distance: Double, targetWidth: Double, targetHeight: Double): Double

    // -- Clicking --

    /** Move to [x], [y] then click with the given [button] (1 = left, 3 = right). */
    fun click(x: Int, y: Int, button: Int = 1)

    /** Click at the current mouse position. */
    fun click(button: Int = 1)

    // -- Drag & Scroll --

    /** Drag from ([fromX], [fromY]) to ([toX], [toY]) while holding [button]. */
    fun drag(fromX: Int, fromY: Int, toX: Int, toY: Int, button: Int = 1)

    /** Scroll the mouse wheel by [amount] (positive = down, negative = up). */
    fun scroll(amount: Int)

    // -- Drift --

    /**
     * Start an asynchronous post-action drift toward [area].
     * Simulates unconscious mouse fidgeting after an action.
     * [delayMs] controls how long to wait before the drift begins.
     */
    fun drift(area: Rectangle, delayMs: Long = 0)

    /** Cancel any active or pending drift. */
    fun cancelDrift()
}
