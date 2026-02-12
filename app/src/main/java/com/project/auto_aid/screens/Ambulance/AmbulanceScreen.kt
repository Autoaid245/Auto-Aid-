package com.project.auto_aid.screens.ambulance

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun AmbulanceScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            "🚑 Ambulance Service",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Immediate emergency medical response", color = Color.Gray)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(Routes.AmbulanceRequestScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("REQUEST AMBULANCE", color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        AmbulanceCard(
            icon = Icons.Default.Phone,
            title = "Request Ambulance",
            subtitle = "Send emergency request"
        ) {
            navController.navigate(Routes.AmbulanceRequestScreen.route)
        }

        AmbulanceCard(
            icon = Icons.Default.Warning,
            title = "Active Request",
            subtitle = "Track ambulance"
        ) {
            navController.navigate(Routes.AmbulanceStatusScreen.route)
        }

        AmbulanceCard(
            icon = Icons.Default.List,
            title = "Request History",
            subtitle = "Past ambulance calls"
        ) {
            navController.navigate(Routes.AmbulanceHistoryScreen.route)
        }
    }
}

@Composable
private fun AmbulanceCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color.Red)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, color = Color.Gray)
            }
        }
    }
}