package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.data.network.RetrofitClient
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.provider.ProviderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderActiveJobScreen(
    requestId: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context) }
    val api = remember(context) { RetrofitClient.create(tokenStore) }
    val vm: ProviderViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var pickupLat by remember { mutableStateOf(0.0) }
    var pickupLng by remember { mutableStateOf(0.0) }

    var currentStatus by remember { mutableStateOf("assigned") }

    fun loadPickup() {
        scope.launch {
            loading = true
            error = null
            try {
                val res = api.getRequestById(requestId)
                if (!res.isSuccessful) {
                    throw Exception("Failed to load request (HTTP ${res.code()})")
                }

                val r = res.body() ?: throw Exception("Request body is null")

                pickupLat = r.userLocation?.lat ?: 0.0
                pickupLng = r.userLocation?.lng ?: 0.0
                currentStatus = r.status ?: "assigned"

                if (pickupLat == 0.0 && pickupLng == 0.0) {
                    error = "User location is missing for this request."
                }
            } catch (e: Throwable) {
                error = e.message ?: "Failed to load pickup location"
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(requestId) { loadPickup() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Job") }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            Text(text = "Request ID: $requestId")
            Spacer(modifier = Modifier.height(12.dp))

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (error != null) {
                Text(text = error!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { loadPickup() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retry Loading Location")
                }
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Pickup Location", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text("Lat: $pickupLat")
                        Text("Lng: $pickupLng")
                        Spacer(Modifier.height(8.dp))
                        Text("Status: $currentStatus")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    navController.navigate(
                        Routes.ProviderMapScreen.createRoute(
                            requestId = requestId,
                            pickupLat = pickupLat,
                            pickupLng = pickupLng
                        )
                    )
                },
                enabled = !loading && error == null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Map")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        vm.updateStatus(requestId, "arrived")
                        currentStatus = "arrived"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Arrived")
                }

                OutlinedButton(
                    onClick = {
                        vm.updateStatus(requestId, "in_progress")
                        currentStatus = "in_progress"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start Job")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.updateStatus(requestId, "completed")
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Complete Job")
            }

            Spacer(modifier = Modifier.height(12.dp))

            ProviderChatPanel(
                requestId = requestId,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}