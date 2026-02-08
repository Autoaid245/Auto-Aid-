package com.project.auto_aid.screens.ambulance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AmbulanceActiveScreen(navController: NavHostController) {

    val statusSteps = listOf(
        "Request Sent",
        "Driver Assigned",
        "Ambulance On The Way",
        "Arrived",
        "Patient Transported",
        "Completed"
    )

    var currentStep by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("🚑 Ambulance Status", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        statusSteps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text(
                    if (index <= currentStep) "✅" else "⬜",
                    modifier = Modifier.width(30.dp)
                )
                Text(step)
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (currentStep < statusSteps.lastIndex) currentStep++
            }
        ) {
            Text("Simulate Next Status")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.popBackStack() }
        ) {
            Text("Back")
        }
    }
}
