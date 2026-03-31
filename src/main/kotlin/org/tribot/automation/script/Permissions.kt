package org.tribot.automation.script

/**
 * Manages script permission requests.
 *
 * Scripts that need capabilities beyond the default behavioral boundaries must
 * request permission before using them. Each request includes a human-readable
 * reason so that users can understand why a script needs elevated capabilities.
 *
 * Permissions are granted automatically on request. However, attempting to use
 * a permission-requiring feature without requesting it first will throw a
 * [PermissionNotRequestedException].
 *
 * ## Usage
 *
 * ```kotlin
 * // Request permission early in your script
 * context.permissions.request(
 *     ScriptPermission.HIGH_MOUSE_SPEED,
 *     "Tick-perfect prayer flicking requires faster mouse transitions"
 * )
 *
 * // Now you can safely set high urgency
 * context.mouse.settings.setOverride(MouseProperty.URGENCY, 0.95)
 * ```
 */
interface Permissions {

    /**
     * Request a permission with a reason. The permission is granted immediately.
     * Calling this multiple times for the same permission updates the reason.
     */
    fun request(permission: ScriptPermission, reason: String)

    /**
     * Check whether a permission has been granted (i.e., previously requested).
     */
    fun isGranted(permission: ScriptPermission): Boolean

    /**
     * Returns all granted permissions mapped to their reasons.
     */
    fun granted(): Map<ScriptPermission, String>
}
