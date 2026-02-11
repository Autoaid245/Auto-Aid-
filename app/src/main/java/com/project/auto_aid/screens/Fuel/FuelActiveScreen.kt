package com.project.auto_aid.screens.fuel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FuelActiveScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }

        Text("Active Fuel Request", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fuel Type: Petrol")
                Text("Quantity: 10 Litres")
                Text("Payment: Cash")
                Text("Status: Searching for vendor")
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { /* later: live map */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Live Status")
        }
    }
}
