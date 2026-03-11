package com.project.auto_aid.screens.ambulance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.data.network.RetrofitClient
import com.project.auto_aid.data.network.dto.CreateRequestBody
import com.project.auto_aid.data.network.dto.RequestDto
import com.project.auto_aid.data.network.dto.UserLocationBody
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmbulanceRequestScreen(
    navController: NavHostController,
    providerId: String? = null,
    userLat: Double = 0.3476,
    userLng: Double = 32.5825
) {
    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context) }
    val api = remember(context) { RetrofitClient.create(tokenStore) }

    var emergencyType by remember { mutableStateOf("Medical Emergency") }
    var patientCondition by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var submitting by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Ambulance") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            if (providerId != null) {
                AssistChip(onClick = {}, label = { Text("Target provider selected") })
            } else {
                AssistChip(onClick = {}, label = { Text("Broadcast to all online ambulance providers") })
            }

            Spacer(Modifier.height(10.dp))

            Text("Emergency Type", fontWeight = FontWeight.SemiBold)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = emergencyType == "Accident",
                    onClick = { emergencyType = "Accident" },
                    label = { Text("Accident") }
                )
                FilterChip(
                    selected = emergencyType == "Medical Emergency",
                    onClick = { emergencyType = "Medical Emergency" },
                    label = { Text("Medical") }
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = emergencyType == "Labour / Pregnancy",
                    onClick = { emergencyType = "Labour / Pregnancy" },
                    label = { Text("Labour") }
                )
                FilterChip(
                    selected = emergencyType == "Breathing Problem",
                    onClick = { emergencyType = "Breathing Problem" },
                    label = { Text("Breathing") }
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = emergencyType == "Unconscious Patient",
                    onClick = { emergencyType = "Unconscious Patient" },
                    label = { Text("Unconscious") }
                )
                FilterChip(
                    selected = emergencyType == "Other",
                    onClick = { emergencyType = "Other" },
                    label = { Text("Other") }
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = patientCondition,
                onValueChange = { patientCondition = it },
                label = { Text("Patient Condition") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("e.g. unconscious, bleeding, severe pain") }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Additional Notes (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("e.g. number of patients, landmark, age, urgent details") }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Current location will be used to help the nearest ambulance find you quickly.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = {
                    if (submitting) return@Button

                    val token = tokenStore.getToken()
                    if (token.isNullOrBlank()) {
                        error = "Please login first."
                        return@Button
                    }

                    if (patientCondition.isBlank()) {
                        error = "Please describe the patient condition."
                        return@Button
                    }

                    submitting = true
                    error = null

                    scope.launch {
                        try {
                            val response = api.createRequest(
                                CreateRequestBody(
                                    service = "ambulance",
                                    providerType = "ambulance",
                                    vehicleInfo = emergencyType.trim(),
                                    problem = buildString {
                                        append(patientCondition.trim())
                                        if (notes.isNotBlank()) {
                                            append(" | Notes: ")
                                            append(notes.trim())
                                        }
                                    },
                                    towType = emergencyType.trim(),
                                    userLocation = UserLocationBody(userLat, userLng),
                                    targetProviderId = providerId
                                )
                            )

                            if (!response.isSuccessful) {
                                throw Exception("Request failed (HTTP ${response.code()})")
                            }

                            val created: RequestDto =
                                response.body() ?: throw Exception("Empty response body")

                            val rid = created._id ?: created.id ?: ""
                            if (rid.isBlank()) throw Exception("Missing request ID")

                            tokenStore.saveLastAmbulanceRequestId(rid)
                            navController.navigate(Routes.AmbulanceActiveScreen.createRoute(rid))
                        } catch (e: Throwable) {
                            error = e.message ?: "Failed to submit."
                        } finally {
                            submitting = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !submitting,
                shape = RoundedCornerShape(14.dp)
            ) {
                if (submitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Request Ambulance", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }

            Spacer(Modifier.height(14.dp))
        }
    }
}