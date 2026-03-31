package org.tribot.automation.script

/**
 * Thrown when a script attempts to use a feature that requires a [ScriptPermission]
 * that has not been requested via [Permissions.request].
 */
class PermissionNotRequestedException(permission: ScriptPermission) : RuntimeException(
    "Permission ${permission.name} is required but was not requested. " +
    "Call context.permissions.request(ScriptPermission.${permission.name}, \"your reason\") before using this feature."
)
