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
     * Executes the given [block] on the client thread and blocks until completion.
     *
     * If the caller is already on the client thread, the block runs immediately. Otherwise, it
     * is submitted to both the work-stealing queue and RuneLite's invoke queue — whichever path
     * executes first wins. This allows multiple calls per game tick when the client thread has
     * idle time, rather than requiring one render cycle per call.
     */
    fun executeBlocking(block: Runnable)

    /**
     * Executes the given [block] on the client thread and returns its result, blocking until
     * completion.
     *
     * Semantically identical to [executeBlocking] (inline if already on the client thread,
     * otherwise submitted and awaited); this overload just forwards the returned value.
     *
     * If [block] throws, the exception is rethrown on the caller's thread. If the underlying
     * dispatcher never ran [block] (e.g. a timeout in the implementation) an
     * [IllegalStateException] is raised rather than silently returning a garbage value.
     */
    fun <T> executeBlocking(block: () -> T): T {
        var result: Any? = null
        var completed = false
        var error: Throwable? = null
        executeBlocking(Runnable {
            try {
                result = block()
                completed = true
            } catch (e: Throwable) {
                error = e
                completed = true
            }
        })
        error?.let { throw it }
        check(completed) { "Client thread execution did not complete" }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    /**
     * Gets Runelite's client thread instance that can invoke methods on the client thread using their technique. This
     * is slower than [executeBlocking].
     */
    fun getRlClientThread(): ClientThread
}