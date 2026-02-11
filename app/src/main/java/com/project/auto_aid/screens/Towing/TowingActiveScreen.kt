package com.project.auto_aid.screens.towing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun TowingActiveScreen(
    navController: NavHostController,
    viewModel: TowingViewModel = viewModel()
) {
    val request by viewModel.request.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Towing Status",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // ================= STATUS =================
        when (request.status) {
            TowingStatus.REQUEST_SENT ->
                StatusText("Request sent. Finding a driver…")

            TowingStatus.DRIVER_ASSIGNED ->
                StatusText("Driver assigned: ${request.driver?.name}")

            TowingStatus.DRIVER_ON_THE_WAY ->
                StatusText("Driver is on the way 🚗")

            TowingStatus.ARRIVED ->
                StatusText("Driver has arrived 📍")

            TowingStatus.VEHICLE_TOWED ->
                StatusText("Vehicle has been towed")

            TowingStatus.COMPLETED ->
                StatusText("Towing completed ✅")

            TowingStatus.CANCELLED ->
                StatusText("Request cancelled ❌")
        }

        Spacer(Modifier.height(20.dp))

        // ================= DRIVER CARD =================
        request.driver?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Driver: ${it.name}")
                    Text("Truck: ${it.truckType}")
                    Text("Plate: ${it.truckPlate}")
                    Text("Rating: ⭐ ${it.rating}")
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (request.status !in listOf(
                TowingStatus.COMPLETED,
                TowingStatus.CANCELLED
            )
        ) {
            OutlinedButton(
                onClick = { viewModel.cancelRequest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel Request")
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back")
        }
    }
}

@Composable
private fun StatusText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge
    )
}
