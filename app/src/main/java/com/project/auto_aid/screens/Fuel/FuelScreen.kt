package com.project.auto_aid.screens.fuel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "⛽ Fuel Delivery",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Order fuel delivered to your exact location",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        FuelOptionCard(
            icon = Icons.Default.Phone,
            title = "Request Fuel",
            subtitle = "Petrol, Diesel, Kerosene"
        ) {
            navController.navigate(Routes.FuelRequestScreen.route)
        }

        FuelOptionCard(
            icon = Icons.Default.Warning,
            title = "Active Request",
            subtitle = "Track your current order"
        ) {
            navController.navigate(Routes.FuelActiveScreen.route)
        }

        FuelOptionCard(
            icon = Icons.Default.List,
            title = "Fuel History",
            subtitle = "View past fuel requests"
        ) {
            navController.navigate(Routes.FuelHistoryScreen.route)
        }
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
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color.Gray
                )
            }
        }
    }
}