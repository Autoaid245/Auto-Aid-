package com.project.auto_aid.screens.fuel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun FuelScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Back")
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "⛽ Fuel Delivery",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Order fuel delivered to your exact location",
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        FuelOptionCard(
            icon = Icons.Default.LocalGasStation,
            title = "Request Fuel",
            subtitle = "Petrol, Diesel, Kerosene",
            onClick = { navController.navigate(Routes.FuelRequestScreen.route) }
        )

        FuelOptionCard(
            icon = Icons.Default.Sync,
            title = "Active Request",
            subtitle = "Track your current order",
            onClick = { navController.navigate(Routes.FuelActiveScreen.route) }
        )

        FuelOptionCard(
            icon = Icons.Default.ReceiptLong,
            title = "Fuel History",
            subtitle = "View past fuel requests",
            onClick = { navController.navigate(Routes.FuelHistoryScreen.route) }
        )
    }
}

@Composable
private fun FuelOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray)
            }
        }
    }
}
