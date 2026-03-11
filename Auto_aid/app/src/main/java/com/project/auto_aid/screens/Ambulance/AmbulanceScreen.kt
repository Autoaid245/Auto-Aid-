package com.project.auto_aid.screens.ambulance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun AmbulanceScreen(navController: NavHostController) {
    val context = LocalContext.current
    val tokenStore = remember(context) { TokenStore(context) }
    val scope = rememberCoroutineScope()

    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(20.dp)
    ) {
        Text(
            text = "Ambulance Service",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Immediate emergency medical response when you need it.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(10.dp))
        }

        AmbulanceServiceCard(
            icon = {
                Icon(
                    Icons.Default.LocalHospital,
                    null,
                    tint = Color.Red
                )
            },
            title = "Request Ambulance",
            subtitle = "Send an emergency ambulance request"
        ) {
            navController.navigate(Routes.AmbulanceRequestScreen.createRoute())
        }

        Spacer(modifier = Modifier.height(16.dp))

        AmbulanceServiceCard(
            icon = {
                Icon(
                    Icons.Default.PlayArrow,
                    null,
                    tint = Color(0xFF16A34A)
                )
            },
            title = "Active Request",
            subtitle = "Track your ongoing ambulance request"
        ) {
            scope.launch {
                val rid = tokenStore.getLastAmbulanceRequestId()
                if (rid.isNullOrBlank()) {
                    error = "No active ambulance request yet. Please request ambulance first."
                    return@launch
                }
                error = null
                navController.navigate(Routes.AmbulanceActiveScreen.createRoute(rid))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AmbulanceServiceCard(
            icon = {
                Icon(
                    Icons.Default.History,
                    null,
                    tint = Color(0xFF0284C7)
                )
            },
            title = "Ambulance History",
            subtitle = "View past ambulance requests"
        ) {
            navController.navigate(Routes.AmbulanceHistoryScreen.route)
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun AmbulanceServiceCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}