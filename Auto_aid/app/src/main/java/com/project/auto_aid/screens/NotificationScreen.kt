package com.project.auto_aid.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "NotifyScreen"

data class UiNotification(
    val id: String,
    val type: String,
    val title: String,
    val body: String,
    val requestId: String?,
    val createdAt: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController) {

    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context) }
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val notifications = remember { mutableStateListOf<UiNotification>() }

    var socket by remember { mutableStateOf<Socket?>(null) }
    var connected by remember { mutableStateOf(false) }

    fun setError(msg: String) {
        scope.launch(Dispatchers.Main) {
            Log.e(TAG, msg)
            error = msg
            loading = false
        }
    }

    fun parseNotifications(arr: JSONArray): List<UiNotification> {
        val list = mutableListOf<UiNotification>()
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue

            val reqId = o.optString("requestId").takeIf { it.isNotBlank() }
            val created = o.optString("createdAt").takeIf { it.isNotBlank() }

            list.add(
                UiNotification(
                    id = o.optString("id").ifBlank { o.optString("_id") },
                    type = o.optString("type", "message"),
                    title = o.optString("title", "Notification"),
                    body = o.optString("body", ""),
                    requestId = reqId,
                    createdAt = created
                )
            )
        }
        return list
    }

    fun requestNotifications(s: Socket) {
        scope.launch(Dispatchers.Main) {
            loading = true
            error = null
        }

        Log.d(TAG, "emit get_notifications")
        s.emit("get_notifications")

        // timeout fallback
        scope.launch {
            delay(8000)
            if (loading && error == null) {
                setError("Timeout waiting for notifications")
            }
        }
    }

    LaunchedEffect(Unit) {

        val token = tokenStore.getToken()
        if (token.isNullOrBlank()) {
            setError("Not logged in")
            return@LaunchedEffect
        }

        try {
            val opts = IO.Options().apply {
                forceNew = true
                reconnection = true
                timeout = 8000

                // ✅ good for real phone
                transports = arrayOf("websocket", "polling")

                extraHeaders = mapOf(
                    "Authorization" to listOf("Bearer $token")
                )
            }

            // ✅ adb reverse => use 127.0.0.1
            val s = IO.socket("http://127.0.0.1:5001", opts)
            socket = s

            s.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "CONNECTED")
                scope.launch(Dispatchers.Main) { connected = true }
                requestNotifications(s)
            }

            s.on(Socket.EVENT_DISCONNECT) {
                Log.d(TAG, "DISCONNECTED")
                scope.launch(Dispatchers.Main) { connected = false }
            }

            s.on(Socket.EVENT_CONNECT_ERROR) { args ->
                val msg = args.firstOrNull()?.toString() ?: "unknown"
                setError("Connect error: $msg")
            }

            // ✅ Your backend emits these
            s.on("notifications") { args ->
                Log.d(TAG, "notifications received")

                try {
                    val payload = args.firstOrNull() as? JSONObject
                    val arr = payload?.optJSONArray("notifications") ?: JSONArray()
                    val parsed = parseNotifications(arr)

                    scope.launch(Dispatchers.Main) {
                        notifications.clear()
                        notifications.addAll(parsed)
                        loading = false
                        error = null
                    }
                } catch (e: Exception) {
                    setError("Parse error: ${e.message}")
                }
            }

            s.on("notifications_error") { args ->
                val payload = args.firstOrNull() as? JSONObject
                val msg = payload?.optString("message") ?: "notifications_error"
                setError(msg)
            }

            Log.d(TAG, "CONNECTING...")
            s.connect()

        } catch (e: Exception) {
            setError("Socket exception: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            socket?.off()
            socket?.disconnect()
            socket = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Loading notifications...")
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { socket?.let { requestNotifications(it) } }) {
                            Text("Retry")
                        }
                    }
                }

                notifications.isEmpty() -> {
                    Text("No notifications yet", modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(notifications, key = { it.id }) { n ->
                            Card(shape = RoundedCornerShape(12.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text(n.title, style = MaterialTheme.typography.titleMedium)
                                    Spacer(Modifier.height(4.dp))
                                    Text(n.body, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}