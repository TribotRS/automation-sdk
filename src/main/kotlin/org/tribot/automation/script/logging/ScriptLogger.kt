package org.tribot.automation.script.logging

import org.tribot.automation.script.event.ListenerRegistration

enum class LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

/** A single log entry produced by the client. */
data class LogEntry(
    val level: LogLevel,
    val message: String,
    val throwable: Throwable? = null,
)

interface ScriptLogger {
    fun log(level: LogLevel, message: String)
    fun log(level: LogLevel, message: String, throwable: Throwable?)
    fun isLevelEnabled(level: LogLevel): Boolean

    /**
     * Called for every log entry the client produces, including logs from this script,
     * other running scripts, and the client itself.
     *
     * Listeners are invoked synchronously on the thread that produced the entry, so they
     * should return quickly. Log calls made from within a listener are not re-delivered
     * to listeners.
     *
     * Listeners are automatically cleaned up when the script ends.
     */
    fun onLog(listener: (entry: LogEntry) -> Unit): ListenerRegistration

    fun trace(message: String) = log(LogLevel.TRACE, message)
    fun debug(message: String) = log(LogLevel.DEBUG, message)
    fun info(message: String) = log(LogLevel.INFO, message)
    fun warn(message: String) = log(LogLevel.WARN, message)
    fun error(message: String) = log(LogLevel.ERROR, message)

    fun warn(message: String, throwable: Throwable?) = log(LogLevel.WARN, message, throwable)
    fun error(message: String, throwable: Throwable?) = log(LogLevel.ERROR, message, throwable)
}

// Kotlin inline extensions for lazy message evaluation.
// The isLevelEnabled guard avoids constructing the message string when the level is disabled.
inline fun ScriptLogger.trace(messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.TRACE)) {
        log(LogLevel.TRACE, messageProvider().toString())
    }
}

inline fun ScriptLogger.debug(messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.DEBUG)) {
        log(LogLevel.DEBUG, messageProvider().toString())
    }
}

inline fun ScriptLogger.info(messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.INFO)) {
        log(LogLevel.INFO, messageProvider().toString())
    }
}

inline fun ScriptLogger.warn(messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.WARN)) {
        log(LogLevel.WARN, messageProvider().toString())
    }
}

inline fun ScriptLogger.error(messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.ERROR)) {
        log(LogLevel.ERROR, messageProvider().toString())
    }
}

inline fun ScriptLogger.error(throwable: Throwable, messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.ERROR)) {
        log(LogLevel.ERROR, messageProvider().toString(), throwable)
    }
}

inline fun ScriptLogger.warn(throwable: Throwable, messageProvider: () -> Any?) {
    if (isLevelEnabled(LogLevel.WARN)) {
        log(LogLevel.WARN, messageProvider().toString(), throwable)
    }
}
