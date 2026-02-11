package com.project.auto_aid.screens.towing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun TowingRequestScreen(navController: NavHostController) {

    var vehicleInfo by remember { mutableStateOf("") }
    var problem by remember { mutableStateOf("") }
    var towType by remember { mutableStateOf("Standard") }
    var submitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Request Towing",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(20.dp))

        // ================= VEHICLE =================
        OutlinedTextField(
            value = vehicleInfo,
            onValueChange = { vehicleInfo = it },
            label = { Text("Vehicle Information") },
            placeholder = { Text("e.g. Toyota Noah, UBB 123X") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(14.dp))

        // ================= ISSUE =================
        OutlinedTextField(
            value = problem,
            onValueChange = { problem = it },
            label = { Text("Problem Description") },
            placeholder = { Text("Engine failure, accident, etc.") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(14.dp))

        // ================= TOW TYPE =================
        Text("Tow Type", fontWeight = FontWeight.SemiBold)

        Row {
            FilterChip(
                selected = towType == "Standard",
                onClick = { towType = "Standard" },
                label = { Text("Standard") }
            )

            Spacer(Modifier.width(10.dp))

            FilterChip(
                selected = towType == "Flatbed",
                onClick = { towType = "Flatbed" },
                label = { Text("Flatbed") }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ================= SUBMIT =================
        Button(
            onClick = {
                if (vehicleInfo.isBlank() || problem.isBlank()) return@Button
                submitting = true
                navController.navigate(Routes.TowingActiveScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
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
                Text("Request Tow", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Cancel")
        }
    }
}
