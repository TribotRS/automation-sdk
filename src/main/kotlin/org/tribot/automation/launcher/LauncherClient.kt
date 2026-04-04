package org.tribot.automation.launcher

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * Client for the Tribot Launcher's public WebSocket API.
 *
 * Provides strongly-typed access to account, proxy, and client management,
 * as well as client function invocation and event subscription.
 *
 * Usage:
 * ```kotlin
 * val client = LauncherClient()
 * client.connect()
 *
 * val accounts = client.accounts.list()
 * val proxies = client.proxies.list()
 * val running = client.clients.list()
 *
 * client.onClientEvent { event ->
 *     println("Event from ${event.clientId}: ${event.eventName}")
 * }
 *
 * client.close()
 * ```
 */
class LauncherClient(
    val host: String = "127.0.0.1",
    val port: Int = 13297,
    val token: String? = null,
    private val tokenProvider: (() -> String)? = null
) : AutoCloseable {

    private val connection = LauncherConnection(host, port, token, tokenProvider)

    val accounts = Accounts()
    val proxies = Proxies()
    val clients = Clients()
    val machines = Machines()

    fun connect() {
        connection.connect()
    }

    override fun close() {
        connection.close()
    }

    // --- Events ---

    fun onClientEvent(listener: (ClientEvent) -> Unit) {
        connection.clientEventListeners.add(listener)
    }

    fun onClientsUpdated(listener: (List<ClientInfo>) -> Unit) {
        connection.clientsUpdatedListeners.add(listener)
    }

    fun onReconnected(listener: () -> Unit) {
        connection.reconnectedListeners.add(listener)
    }

    fun onDisconnected(listener: (Throwable?) -> Unit) {
        connection.disconnectedListeners.add(listener)
    }

    // --- Accounts ---

    inner class Accounts {

        fun list(): List<Account> {
            val result = connection.rpcCall("get_accounts")
            val raw = result.getAsJsonArray("value") ?: return emptyList()
            return raw.map { parseAccount(it.asJsonObject) }
        }

        fun add(
            username: String,
            password: String,
            world: Int? = null,
            bankPin: String? = null,
            tags: String? = null,
            notes: String? = null,
            proxyId: Int? = null
        ): Int {
            val params = mutableMapOf<String, Any?>(
                "username" to username,
                "password" to password
            )
            world?.let { params["world"] = it }
            bankPin?.let { params["bank_pin"] = it }
            tags?.let { params["tags"] = it }
            notes?.let { params["notes"] = it }
            proxyId?.let { params["proxy_id"] = it }

            val result = connection.rpcCall("add_legacy_account", params)
            return result.get("id")?.asInt ?: throw RpcException(-32603, "Missing id in response")
        }

        fun save(
            accountId: Int,
            username: String? = null,
            password: String? = null,
            world: Int? = null,
            bankPin: String? = null,
            tags: String? = null,
            notes: String? = null,
            proxyId: Int? = null
        ) {
            val params = mutableMapOf<String, Any?>("account_id" to accountId)
            username?.let { params["username"] = it }
            password?.let { params["password"] = it }
            world?.let { params["world"] = it }
            bankPin?.let { params["bank_pin"] = it }
            tags?.let { params["tags"] = it }
            notes?.let { params["notes"] = it }
            proxyId?.let { params["proxy_id"] = it }

            connection.rpcCall("save_account", params)
        }

        fun delete(accountId: Int) {
            connection.rpcCall("delete_account", mapOf("account_id" to accountId))
        }
    }

    // --- Proxies ---

    inner class Proxies {

        fun list(): List<Proxy> {
            val result = connection.rpcCall("get_proxies")
            val raw = result.getAsJsonArray("value") ?: return emptyList()
            return raw.map { parseProxy(it.asJsonObject) }
        }

        fun add(
            name: String,
            host: String,
            port: Int,
            username: String? = null,
            password: String? = null
        ): Int {
            val params = mutableMapOf<String, Any?>(
                "name" to name,
                "host" to host,
                "port" to port
            )
            username?.let { params["username"] = it }
            password?.let { params["password"] = it }

            val result = connection.rpcCall("add_proxy", params)
            return result.get("id")?.asInt ?: throw RpcException(-32603, "Missing id in response")
        }

        fun save(
            proxyId: Int,
            name: String,
            host: String,
            port: Int,
            username: String? = null,
            password: String? = null
        ) {
            val params = mutableMapOf<String, Any?>(
                "proxy_id" to proxyId,
                "name" to name,
                "host" to host,
                "port" to port
            )
            username?.let { params["username"] = it }
            password?.let { params["password"] = it }

            connection.rpcCall("save_proxy", params)
        }

        fun delete(proxyId: Int) {
            connection.rpcCall("delete_proxy", mapOf("proxy_id" to proxyId))
        }

        fun test(proxyId: Int): Boolean {
            val result = connection.rpcCall("test_proxy", mapOf("proxy_id" to proxyId))
            return result.get("success")?.asBoolean ?: false
        }
    }

    // --- Clients ---

    inner class Clients {

        private val gson = Gson()

        fun list(): List<ClientInfo> {
            val result = connection.rpcCall("get_clients")
            val clientsArray = result.getAsJsonArray("clients") ?: return emptyList()
            return clientsArray.map { element ->
                val entry = element.asJsonObject
                val clientId = entry.get("client_id")?.asString ?: ""
                val dataObj = entry.get("data")?.takeIf { !it.isJsonNull }?.asJsonObject ?: JsonObject()
                ClientInfo(clientId = clientId, data = parseClientData(dataObj))
            }
        }

        fun launch(config: LaunchConfig = LaunchConfig()) {
            val params = mutableMapOf<String, Any?>()
            config.accountId?.let { params["account_id"] = it }
            config.jagexCharacterName?.let { params["jagex_character_name"] = it }
            config.jagexCharacterId?.let { params["jagex_character_id"] = it }
            config.jagexCharacterIdRaw?.let { params["jagex_character_id_raw"] = it }
            config.jagexSessionIdRaw?.let { params["jagex_session_id_raw"] = it }
            config.jagexCharacterNameRaw?.let { params["jagex_character_name_raw"] = it }
            config.legacyUsername?.let { params["legacy_username"] = it }
            config.legacyPasswordRaw?.let { params["legacy_password_raw"] = it }
            config.legacyTotpRaw?.let { params["legacy_totp_raw"] = it }
            config.bankPinRaw?.let { params["bank_pin_raw"] = it }
            config.scriptName?.let { params["script_name"] = it }
            config.scriptArgs?.let { params["script_args"] = it }
            config.proxyName?.let { params["proxy_name"] = it }
            config.proxyHostRaw?.let { params["proxy_host_raw"] = it }
            config.proxyPortRaw?.let { params["proxy_port_raw"] = it }
            config.proxyUsernameRaw?.let { params["proxy_username_raw"] = it }
            config.proxyPasswordRaw?.let { params["proxy_password_raw"] = it }
            config.heapMb?.let { params["heap_mb"] = it }
            config.clientName?.let { params["client_name"] = it }
            config.world?.let { params["world"] = it }
            config.breakProfileName?.let { params["break_profile_name"] = it }
            config.minimized?.let { params["minimized"] = it }
            config.windowWidth?.let { params["window_width"] = it }
            config.windowHeight?.let { params["window_height"] = it }
            config.windowX?.let { params["window_x"] = it }
            config.windowY?.let { params["window_y"] = it }

            connection.rpcCall("launch_client", params)
        }

        fun terminate(clientId: String) {
            connection.rpcCall("terminate_client", mapOf("client_id" to clientId))
        }

        fun minimize(clientId: String) {
            connection.rpcCall("minimize_client", mapOf("client_id" to clientId))
        }

        fun maximize(clientId: String) {
            connection.rpcCall("maximize_client", mapOf("client_id" to clientId))
        }

        fun setSize(clientId: String, width: Int, height: Int) {
            connection.rpcCall("set_client_size", mapOf("client_id" to clientId, "width" to width, "height" to height))
        }

        fun setPosition(clientId: String, x: Int, y: Int) {
            connection.rpcCall("set_client_position", mapOf("client_id" to clientId, "x" to x, "y" to y))
        }

        fun getFunctions(clientId: String): List<String> {
            val result = connection.rpcCall("get_client_functions", mapOf("client_id" to clientId))
            val arr = result.getAsJsonArray("functions") ?: return emptyList()
            return arr.map { it.asString }
        }

        fun callFunction(clientId: String, name: String, input: String? = null): String {
            val params = mutableMapOf<String, Any?>(
                "client_id" to clientId,
                "function_name" to name
            )
            input?.let { params["input"] = it }

            val result = connection.rpcCall("call_client_function", params)
            return result.get("result")?.let { if (it.isJsonNull) "" else it.asString } ?: ""
        }

        // --- Script control ---

        fun getScripts(clientId: String): List<ClientScriptData> {
            val json = callFunction(clientId, "tribot_automation_get_scripts")
            if (json.isBlank()) return emptyList()
            return gson.fromJson(json, object : TypeToken<List<ClientScriptData>>() {}.type)
        }

        fun getRunningScript(clientId: String): ClientScriptInfo? {
            val json = callFunction(clientId, "tribot_automation_get_running_script")
            if (json.isBlank()) return null
            return gson.fromJson(json, ClientScriptInfo::class.java)
        }

        fun runScript(clientId: String, scriptName: String, args: String = ""): ClientScriptStartResult {
            val input = gson.toJson(mapOf("name" to scriptName, "args" to args))
            val json = callFunction(clientId, "tribot_automation_run_script", input)
            val obj = gson.fromJson(json, JsonObject::class.java)
            return if (obj.has("runId")) {
                ClientScriptStartResult.Success(runId = obj.get("runId").asString)
            } else {
                ClientScriptStartResult.Error(reason = obj.get("reason")?.asString ?: "Unknown error")
            }
        }

        fun stopScript(clientId: String) {
            callFunction(clientId, "tribot_automation_stop_script")
        }

        fun pauseScript(clientId: String) {
            callFunction(clientId, "tribot_automation_pause_script")
        }

        fun resumeScript(clientId: String) {
            callFunction(clientId, "tribot_automation_resume_script")
        }
    }

    // --- Machines ---

    inner class Machines {
        fun list(): List<MachineInfo> {
            val result = connection.rpcCall("get_machines")
            val raw = result.getAsJsonArray("value") ?: return emptyList()
            return raw.map { parseMachineInfo(it.asJsonObject) }
        }
    }

    // --- Token Generation ---

    fun generateConnectionToken(): ConnectionTokenResponse {
        val result = connection.rpcCall("generate_connection_token")
        return ConnectionTokenResponse(
            token = result.get("token")?.asString ?: throw RpcException(-32603, "Missing token in response"),
            expiresInSeconds = result.get("expires_in_seconds")?.asLong ?: throw RpcException(-32603, "Missing expires_in_seconds in response")
        )
    }

    companion object {
        /**
         * Connect to the local launcher (no authentication required).
         */
        fun local(port: Int = 13297): LauncherClient {
            val client = LauncherClient(host = "127.0.0.1", port = port)
            client.connect()
            return client
        }

        /**
         * Connect to a remote launcher. Automatically generates a connection token
         * via the local launcher, then uses it to authenticate with the remote.
         *
         * @param host The remote launcher's hostname or IP address
         * @param port The remote launcher's port (default 13297)
         * @param localPort The local launcher's port for token generation (default 13297)
         */
        fun remote(host: String, port: Int = 13297, localPort: Int = 13297): LauncherClient {
            // Generate initial token
            val local = local(localPort)
            val tokenResponse = try {
                local.generateConnectionToken()
            } finally {
                local.close()
            }

            // Create token provider for reconnection
            val provider: () -> String = {
                val refreshLocal = LauncherClient(host = "127.0.0.1", port = localPort)
                refreshLocal.connect()
                val resp = try {
                    refreshLocal.generateConnectionToken()
                } finally {
                    refreshLocal.close()
                }
                resp.token
            }

            val client = LauncherClient(host = host, port = port, token = tokenResponse.token, tokenProvider = provider)
            client.connect()
            return client
        }

        /**
         * Connect to a registered machine. Automatically generates a connection token
         * via the local launcher, then uses it to authenticate with the remote machine.
         *
         * @param machine The machine to connect to (from machines.list())
         * @param localPort The local launcher's port for token generation (default 13297)
         */
        fun connectToMachine(machine: MachineInfo, localPort: Int = 13297): LauncherClient {
            val host = machine.ip.ifEmpty { machine.autoIp }
            require(host.isNotEmpty()) { "Machine '${machine.name}' has no IP address configured" }
            return remote(host = host, port = machine.port, localPort = localPort)
        }
    }

    // --- Parsing helpers ---

    private fun parseAccount(data: JsonObject): Account {
        val jagexAccountObj = data.get("jagex_account")?.takeIf { !it.isJsonNull }?.asJsonObject
        val proxyObj = data.get("proxy")?.takeIf { !it.isJsonNull }?.asJsonObject
        return Account(
            id = data.get("id")?.asInt ?: 0,
            legacyLoginName = data.get("legacy_login_name")?.takeIf { !it.isJsonNull }?.asString,
            jagexCharacter = data.get("jagex_character")?.takeIf { !it.isJsonNull }?.asString,
            jagexAccount = jagexAccountObj?.let {
                JagexAccountInfo(
                    characterId = it.get("character_id")?.asString ?: "",
                    characterDisplayName = it.get("character_display_name")?.asString ?: "",
                    accountDisplayName = it.get("account_display_name")?.takeIf { f -> !f.isJsonNull }?.asString
                )
            },
            proxyId = data.get("proxy_id")?.takeIf { !it.isJsonNull }?.asInt,
            proxy = proxyObj?.let {
                ProxyRef(
                    id = it.get("id")?.asInt ?: 0,
                    name = it.get("name")?.asString ?: ""
                )
            },
            preferredWorld = data.get("preferred_world")?.takeIf { !it.isJsonNull }?.asInt,
            bankPin = data.get("bank_pin")?.takeIf { !it.isJsonNull }?.asString,
            tags = data.get("tags")?.takeIf { !it.isJsonNull }?.asString,
            notes = data.get("notes")?.takeIf { !it.isJsonNull }?.asString
        )
    }

    private fun parseProxy(data: JsonObject): Proxy {
        return Proxy(
            id = data.get("id")?.asInt ?: 0,
            name = data.get("name")?.asString ?: "",
            host = data.get("host")?.asString ?: "",
            port = data.get("port")?.asInt ?: 0,
            username = data.get("username")?.takeIf { !it.isJsonNull }?.asString,
            password = data.get("password")?.takeIf { !it.isJsonNull }?.asString
        )
    }

    private fun parseMachineInfo(data: JsonObject): MachineInfo {
        return MachineInfo(
            id = data.get("id")?.asInt ?: 0,
            name = data.get("name")?.asString ?: "",
            ip = data.get("ip")?.asString ?: "",
            autoIp = data.get("auto_ip")?.asString ?: "",
            port = data.get("port")?.asInt ?: 13297,
            description = data.get("description")?.asString ?: "",
            syncId = data.get("sync_id")?.takeIf { !it.isJsonNull }?.asString,
            syncUpdatedAt = data.get("sync_updated_at")?.takeIf { !it.isJsonNull }?.asLong
        )
    }

    private fun parseClientData(data: JsonObject): ClientData {
        val scriptObj = data.get("running_script")?.takeIf { !it.isJsonNull }?.asJsonObject
        val runningScript = scriptObj?.let {
            RunningScriptData(
                name = it.get("name")?.asString ?: "",
                startTimeMillis = it.get("start_time_millis")?.asLong ?: 0,
                legacyAccountName = it.get("legacy_account_name")?.asString ?: ""
            )
        }
        return ClientData(
            friendlyName = data.get("friendly_name")?.asString ?: "",
            version = data.get("version")?.asString ?: "",
            runningScript = runningScript,
            proxy = data.get("proxy")?.takeIf { !it.isJsonNull }?.asString,
            accountName = data.get("account_name")?.takeIf { !it.isJsonNull }?.asString,
            inGameName = data.get("in_game_name")?.takeIf { !it.isJsonNull }?.asString,
            loggedIn = data.get("logged_in")?.takeIf { !it.isJsonNull }?.asBoolean
        )
    }
}
