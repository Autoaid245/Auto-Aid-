package com.project.auto_aid.screens.fuel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun FuelRequestScreen(navController: NavHostController) {

    var fuelType by remember { mutableStateOf("Petrol") }
    var quantity by remember { mutableStateOf("10") }
    var payment by remember { mutableStateOf("Cash") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }

        Text("Request Fuel", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        // Fuel Type
        DropdownField(
            label = "Fuel Type",
            options = listOf("Petrol", "Diesel", "Kerosene"),
            selected = fuelType,
            onSelect = { fuelType = it }
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity (Litres)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        DropdownField(
            label = "Payment Method",
            options = listOf("Cash", "Mobile Money"),
            selected = payment,
            onSelect = { payment = it }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                loading = true
                navController.navigate(Routes.FuelActiveScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            enabled = !loading
        ) {
            Text(if (loading) "Requesting…" else "Request Fuel")
        }
    }
}

@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label)
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
