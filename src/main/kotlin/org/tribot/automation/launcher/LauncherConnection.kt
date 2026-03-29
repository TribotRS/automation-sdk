package org.tribot.automation.launcher

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

internal class LauncherConnection(
    private val host: String,
    private val port: Int,
    private val token: String? = null,
    private val tokenProvider: (() -> String)? = null
) {
    private var client: OkHttpClient? = null
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val pendingCalls = ConcurrentHashMap<String, CompletableFuture<JsonObject>>()
    internal val clientEventListeners = CopyOnWriteArrayList<(ClientEvent) -> Unit>()
    internal val clientsUpdatedListeners = CopyOnWriteArrayList<(List<ClientInfo>) -> Unit>()
    internal val reconnectedListeners = CopyOnWriteArrayList<() -> Unit>()
    internal val disconnectedListeners = CopyOnWriteArrayList<(Throwable?) -> Unit>()

    @Volatile
    private var reconnecting = false

    @Volatile
    private var intentionallyClosed = false

    private var currentToken: String? = token

    fun connect() {
        val okClient = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        client = okClient

        val url = buildUrl(currentToken)
        val request = Request.Builder()
            .url(url)
            .build()

        val latch = CountDownLatch(1)
        var connectError: Throwable? = null

        val ws = okClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                latch.countDown()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleMessage(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (!intentionallyClosed) {
                    disconnectedListeners.forEach { it(null) }
                }
                pendingCalls.values.forEach {
                    it.completeExceptionally(IllegalStateException("Connection closed"))
                }
                pendingCalls.clear()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                if (latch.count > 0) {
                    // Initial connection failure
                    connectError = t
                    latch.countDown()
                    return
                }
                // Unexpected disconnect — attempt reconnection
                if (!intentionallyClosed && !reconnecting) {
                    disconnectedListeners.forEach { it(t) }
                    attemptReconnect()
                } else {
                    pendingCalls.values.forEach { it.completeExceptionally(t) }
                    pendingCalls.clear()
                }
            }
        })

        webSocket = ws

        if (!latch.await(10, TimeUnit.SECONDS)) {
            ws.cancel()
            throw IllegalStateException("WebSocket connection timed out")
        }
        connectError?.let { throw IllegalStateException("WebSocket connection failed", it) }
    }

    private fun buildUrl(token: String?): String {
        return if (token != null) {
            "ws://$host:$port/api/wss?token=$token"
        } else {
            "ws://$host:$port/api/wss"
        }
    }

    private fun attemptReconnect() {
        reconnecting = true
        Thread {
            val delays = longArrayOf(1000, 2000, 4000, 8000, 16000)
            var connected = false

            for (attempt in delays.indices) {
                try {
                    Thread.sleep(delays[attempt])
                } catch (_: InterruptedException) {
                    break
                }

                // Refresh token if provider is available
                if (tokenProvider != null) {
                    try {
                        currentToken = tokenProvider.invoke()
                    } catch (_: Exception) {
                        continue  // Token refresh failed, try again
                    }
                }

                try {
                    connect()
                    connected = true
                    break
                } catch (_: Exception) {
                    // Connection attempt failed, will retry
                }
            }

            reconnecting = false

            if (connected) {
                reconnectedListeners.forEach { it() }
            } else {
                // All retries failed
                pendingCalls.values.forEach {
                    it.completeExceptionally(IllegalStateException("Reconnection failed after all retries"))
                }
                pendingCalls.clear()
            }
        }.start()
    }

    fun close() {
        intentionallyClosed = true
        webSocket?.close(1000, null)
        client?.dispatcher?.executorService?.shutdown()
        pendingCalls.values.forEach {
            it.completeExceptionally(IllegalStateException("Connection closed"))
        }
        pendingCalls.clear()
    }

    fun rpcCall(method: String, params: Map<String, Any?>? = null): JsonObject {
        if (reconnecting) {
            throw IllegalStateException("Connection is reconnecting, please retry")
        }

        val id = UUID.randomUUID().toString()
        val future = CompletableFuture<JsonObject>()
        pendingCalls[id] = future

        val request = JsonObject().apply {
            addProperty("id", id)
            addProperty("method", method)
            if (params != null) {
                add("params", gson.toJsonTree(params))
            }
        }

        val ws = webSocket ?: throw IllegalStateException("Not connected")
        ws.send(gson.toJson(request))

        return try {
            future.get(60, TimeUnit.SECONDS)
        } finally {
            pendingCalls.remove(id)
        }
    }

    private fun handleMessage(text: String) {
        val json = try {
            JsonParser.parseString(text).asJsonObject
        } catch (_: Exception) {
            return
        }

        // Check if it's a response (has "id")
        val id = json.get("id")?.takeIf { !it.isJsonNull }?.asString
        if (id != null && pendingCalls.containsKey(id)) {
            val error = json.get("error")?.takeIf { !it.isJsonNull }
            if (error != null) {
                val errorObj = error.asJsonObject
                val code = errorObj.get("code")?.asInt ?: -32603
                val message = errorObj.get("message")?.asString ?: "Unknown error"
                pendingCalls[id]?.completeExceptionally(RpcException(code, message))
            } else {
                val result = json.get("result")
                val resultObj = if (result != null && result.isJsonObject) {
                    result.asJsonObject
                } else {
                    JsonObject().apply { add("value", result) }
                }
                pendingCalls[id]?.complete(resultObj)
            }
            return
        }

        // It's a notification
        val method = json.get("method")?.asString ?: return
        val params = json.get("params")?.takeIf { it.isJsonObject }?.asJsonObject ?: return

        when (method) {
            "client_event" -> {
                val event = ClientEvent(
                    clientId = params.get("client_id")?.asString ?: "",
                    eventName = params.get("event_name")?.asString ?: "",
                    eventData = params.get("event_data")?.asString ?: ""
                )
                clientEventListeners.forEach { it(event) }
            }
            "clients_updated" -> {
                val clients = parseClientList(params)
                clientsUpdatedListeners.forEach { it(clients) }
            }
        }
    }

    private fun parseClientList(data: JsonObject): List<ClientInfo> {
        val clientsArray = data.getAsJsonArray("clients") ?: return emptyList()
        return clientsArray.map { element ->
            val entry = element.asJsonObject
            val clientId = entry.get("client_id")?.asString ?: ""
            val dataObj = entry.get("data")?.takeIf { !it.isJsonNull }?.asJsonObject ?: JsonObject()
            ClientInfo(
                clientId = clientId,
                data = parseClientData(dataObj)
            )
        }
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

class RpcException(val code: Int, override val message: String) : Exception(message)
