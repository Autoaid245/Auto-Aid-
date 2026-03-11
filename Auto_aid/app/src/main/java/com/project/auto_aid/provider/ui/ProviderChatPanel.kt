package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.auto_aid.data.local.TokenStore
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException

@Composable
fun ProviderChatPanel(
    requestId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context) }
    val scope = rememberCoroutineScope()

    var chatExpanded by remember { mutableStateOf(true) }
    var chatInput by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatUiMessage>() }

    var chatSocket by remember { mutableStateOf<Socket?>(null) }
    var chatConnected by remember { mutableStateOf(false) }
    var joinedOnce by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun markReadSafe(s: Socket?) {
        runCatching { s?.emit("markRead", JSONObject().put("requestId", requestId)) }
    }

    // ✅ Connect once per requestId
    LaunchedEffect(requestId) {
        if (chatSocket != null) return@LaunchedEffect

        val token = tokenStore.getToken()
        if (token.isNullOrBlank()) {
            error = "Provider not logged in. Login again to use chat."
            return@LaunchedEffect
        }

        try {
            val opts = IO.Options().apply {
                forceNew = true
                reconnection = true
                extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
            }

            // ✅ adb reverse tcp:5001 tcp:5001
            val s = IO.socket("http://127.0.0.1:5001", opts)
            chatSocket = s

            s.on(Socket.EVENT_CONNECT) {
                chatConnected = true
                s.emit("joinChat", JSONObject().put("requestId", requestId))
                markReadSafe(s)
            }

            s.on(Socket.EVENT_DISCONNECT) { chatConnected = false }

            s.on(Socket.EVENT_CONNECT_ERROR) { args ->
                error = "Chat connect error: ${args.firstOrNull() ?: "unknown"}"
            }

            s.on("chat_error") { args ->
                val payload = args.firstOrNull() as? JSONObject
                error = payload?.optString("message") ?: "Chat error"
            }

            s.on("chat_joined") { joinedOnce = true }

            s.on("chat_history") { args ->
                runCatching {
                    val payload = args.firstOrNull() as? JSONObject ?: return@runCatching
                    val arr = payload.optJSONArray("messages") ?: JSONArray()

                    messages.clear()
                    for (i in 0 until arr.length()) {
                        val m = arr.getJSONObject(i)
                        val ui = m.toUi()
                        if (ui.id.isNotBlank() && messages.none { it.id == ui.id }) {
                            messages.add(ui)
                        }
                    }
                    joinedOnce = true
                }
            }

            s.on("new_message") { args ->
                runCatching {
                    val payload = args.firstOrNull() as? JSONObject ?: return@runCatching
                    val m = payload.optJSONObject("message") ?: return@runCatching
                    val ui = m.toUi()

                    if (ui.id.isNotBlank() && messages.none { it.id == ui.id }) {
                        messages.add(ui)
                    }
                    markReadSafe(s)
                }
            }

            s.connect()
        } catch (e: URISyntaxException) {
            error = "Chat socket URL invalid: ${e.message}"
        } catch (e: Exception) {
            error = "Chat socket error: ${e.message}"
        }
    }

    // ✅ Cleanup
    DisposableEffect(requestId) {
        onDispose {
            runCatching {
                chatSocket?.emit("leaveChat", JSONObject().put("requestId", requestId))
                chatSocket?.disconnect()
                chatSocket?.off()
            }
            chatSocket = null
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Chat with User", fontWeight = FontWeight.Bold)
                    Text(
                        text = if (chatConnected) "Connected" else "Connecting…",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (!joinedOnce) {
                        Text(
                            "Joining chat…",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    error?.let {
                        Spacer(Modifier.height(4.dp))
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
                TextButton(onClick = { chatExpanded = !chatExpanded }) {
                    Text(if (chatExpanded) "Hide" else "Open")
                }
            }

            if (chatExpanded) {
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 320.dp)
                ) {
                    items(messages, key = { it.id }) { m ->
                        ProviderChatBubble(m)
                        Spacer(Modifier.height(6.dp))
                    }
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = chatInput,
                    onValueChange = { chatInput = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Type message…") },
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        val s = chatSocket ?: return@Button
                        val clean = chatInput.trim()
                        if (clean.isBlank()) return@Button

                        messages.add(
                            ChatUiMessage(
                                id = "local_${System.currentTimeMillis()}",
                                sender = "provider",
                                text = clean,
                                createdAt = ""
                            )
                        )

                        s.emit(
                            "sendMessage",
                            JSONObject()
                                .put("requestId", requestId)
                                .put("text", clean)
                        )
                        chatInput = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = chatSocket?.connected() == true
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Send")
                }
            }
        }
    }
}

/* =========================
   Chat helpers
========================= */

data class ChatUiMessage(
    val id: String,
    val sender: String,
    val text: String,
    val createdAt: String
)

private fun JSONObject.toUi(): ChatUiMessage {
    val id = optString("_id").ifBlank { optString("id") }
    return ChatUiMessage(
        id = id,
        sender = optString("sender"),
        text = optString("text"),
        createdAt = optString("createdAt")
    )
}

@Composable
fun ProviderChatBubble(m: ChatUiMessage) {
    val isMe = m.sender.lowercase() == "provider"
    val bg =
        if (isMe) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant

    val fg =
        if (isMe) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(color = bg, shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(10.dp)) { Text(m.text, color = fg) }
        }
    }
}