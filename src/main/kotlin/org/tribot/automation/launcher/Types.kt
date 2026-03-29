package org.tribot.automation.launcher

data class Account(
    val id: Int,
    val legacyLoginName: String?,
    val jagexCharacter: String?,
    val jagexAccount: JagexAccountInfo?,
    val proxyId: Int?,
    val proxy: ProxyRef?,
    val preferredWorld: Int?,
    val bankPin: String?,
    val tags: String?,
    val notes: String?
)

data class JagexAccountInfo(
    val characterId: String,
    val characterDisplayName: String,
    val accountDisplayName: String?
)

data class ProxyRef(
    val id: Int,
    val name: String
)

data class Proxy(
    val id: Int,
    val name: String,
    val host: String,
    val port: Int,
    val username: String?,
    val password: String?
)

data class ClientInfo(
    val clientId: String,
    val data: ClientData
)

data class ClientData(
    val friendlyName: String = "",
    val version: String = "",
    val runningScript: RunningScriptData? = null,
    val proxy: String? = null,
    val accountName: String? = null,
    val inGameName: String? = null,
    val loggedIn: Boolean? = null
)

data class RunningScriptData(
    val name: String,
    val startTimeMillis: Long,
    val legacyAccountName: String
)

data class MachineInfo(
    val id: Int,
    val name: String,
    val ip: String,
    val autoIp: String,
    val port: Int,
    val description: String,
    val syncId: String?,
    val syncUpdatedAt: Long?
)

data class LaunchConfig(
    val accountId: Int? = null,
    val jagexCharacterName: String? = null,
    val jagexCharacterId: String? = null,
    val jagexCharacterIdRaw: String? = null,
    val jagexSessionIdRaw: String? = null,
    val jagexCharacterNameRaw: String? = null,
    val legacyUsername: String? = null,
    val legacyPasswordRaw: String? = null,
    val legacyTotpRaw: String? = null,
    val bankPinRaw: String? = null,
    val scriptName: String? = null,
    val scriptArgs: String? = null,
    val proxyName: String? = null,
    val proxyHostRaw: String? = null,
    val proxyPortRaw: Int? = null,
    val proxyUsernameRaw: String? = null,
    val proxyPasswordRaw: String? = null,
    val heapMb: Int? = null,
    val clientName: String? = null,
    val world: Int? = null,
    val breakProfileName: String? = null,
    val minimized: Boolean? = null,
    val windowWidth: Int? = null,
    val windowHeight: Int? = null,
    val windowX: Int? = null,
    val windowY: Int? = null
)

data class ConnectionTokenResponse(
    val token: String,
    val expiresInSeconds: Long
)
