package com.project.auto_aid.screens.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun GarageRequestScreen(navController: NavHostController) {

    var vehicle by remember { mutableStateOf("") }
    var issue by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("Car") }
    var urgency by remember { mutableStateOf("Normal") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // ================= BACK =================
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ================= TITLE =================
        Text(
            text = "Request Garage Assistance",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Provide details so we can send the right mechanic.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ================= VEHICLE TYPE =================
        Text("Vehicle Type", fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Car", "Motorcycle", "Truck", "Other").forEach {
                FilterChip(
                    selected = vehicleType == it,
                    onClick = { vehicleType = it },
                    label = { Text(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= VEHICLE INFO =================
        OutlinedTextField(
            value = vehicle,
            onValueChange = { vehicle = it },
            label = { Text("Vehicle Information") },
            placeholder = { Text("e.g. Toyota Wish, UBL 123A") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ================= ISSUE =================
        OutlinedTextField(
            value = issue,
            onValueChange = { issue = it },
            label = { Text("Describe the Issue") },
            placeholder = { Text("e.g. Engine won’t start") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ================= URGENCY =================
        Text("Urgency", fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Normal", "Urgent 🚨").forEach {
                FilterChip(
                    selected = urgency == it,
                    onClick = { urgency = it },
                    label = { Text(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= LOCATION =================
        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📍 Use My Current Location")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your location helps us send the nearest mechanic.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ================= SUBMIT (FIXED) =================
        Button(
            onClick = {
                if (vehicle.isBlank() || issue.isBlank()) return@Button

                loading = true

                // ✅ THIS IS THE FIX
                navController.navigate(Routes.RequestProcessingScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            enabled = !loading
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send Request",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= TRUST MESSAGE =================
        Text(
            text = "✔ Your request will be sent to the nearest verified mechanic.",
            fontSize = 12.sp,
            color = Color(0xFF16A34A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}
