package org.tribot.automation.script.core

import net.runelite.client.callback.ClientThread

/**
 * Provides the ability to execute code on the game's client thread.
 *
 * Unlike RuneLite's [net.runelite.client.callback.ClientThread], which queues work to run once
 * per client frame (on the next render tick), this implementation uses a work-stealing approach
 * that can execute queued work within the current frame. This means multiple [executeBlocking]
 * calls can be serviced in the same game tick, rather than each one waiting for the next render
 * cycle.
 *
 * The work-stealing mechanism drains a shared work queue during idle time on the client thread
 * (e.g., during `Thread.sleep` calls within the game loop). If the work queue isn't drained in
 * time, execution falls back to RuneLite's standard invoke path on the next client tick.
 */
interface ClientThread {

    /**
     * Executes [block] on the client thread and blocks until it completes, returning whatever
     * [block] returns. If the caller is already on the client thread, [block] runs inline.
     *
     * The return type is nullable so that legitimate `null` returns from Java-side APIs (e.g.
     * `client.localPlayer`) flow through correctly instead of silently passing an unchecked cast.
     * A `null` return means the block ran and returned null — never that it timed out or threw.
     *
     * If [block] throws, the exception is rethrown on the caller's thread.
     */
    fun <T> executeBlocking(block: () -> T): T?

    /**
     * Convenience shim over the generic form for callers that don't need a return value (and for
     * Java interop via SAM conversion).
     */
    fun executeBlocking(block: Runnable) {
        executeBlocking<Unit> { block.run() }
    }

    /**
     * Gets Runelite's client thread instance that can invoke methods on the client thread using their technique. This
     * is slower than [executeBlocking].
     */
    fun getRlClientThread(): ClientThread
}
