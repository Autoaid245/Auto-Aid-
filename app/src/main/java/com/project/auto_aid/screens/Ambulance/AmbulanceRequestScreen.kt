package com.project.auto_aid.screens.ambulance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun AmbulanceRequestScreen(navController: NavHostController) {

    var step by rememberSaveable { mutableStateOf(1) }
    var emergencyType by rememberSaveable { mutableStateOf("") }
    var condition by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("🚨 Emergency Request", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        when (step) {

            1 -> {
                OutlinedTextField(
                    value = emergencyType,
                    onValueChange = { emergencyType = it },
                    label = { Text("Emergency Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { step = 2 },
                    enabled = emergencyType.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }

            2 -> {
                OutlinedTextField(
                    value = condition,
                    onValueChange = { condition = it },
                    label = { Text("Patient Condition") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Additional Notes") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.AmbulanceStatusScreen.route) {
                            launchSingleTop = true
                        }
                    },
                    enabled = condition.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Request Ambulance")
                }
            }
        }
    }
}
