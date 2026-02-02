package com.project.auto_aid.screens.ambulance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AmbulanceStatusScreen(navController: NavHostController) {

    val steps = listOf(
        "Request Sent",
        "Driver Assigned",
        "On The Way",
        "Arrived",
        "Completed"
    )

    var currentStep by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("🚑 Ambulance Status", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        steps.forEachIndexed { index, step ->
            Text(
                text = if (index <= currentStep) "✅ $step" else "⬜ $step",
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (currentStep < steps.lastIndex) currentStep++
            }
        ) {
            Text("Update Status")
        }
    }
}
