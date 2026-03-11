package com.project.auto_aid.screens.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun GarageScreen(navController: NavHostController) {
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
            text = "Garage Service",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Fast, reliable mechanical help when you need it.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(10.dp))
        }

        GarageServiceCard(
            icon = { Icon(Icons.Default.Build, null, tint = MaterialTheme.colorScheme.primary) },
            title = "Request Garage Help",
            subtitle = "Get immediate mechanic assistance"
        ) {
            navController.navigate(Routes.GarageRequestScreen.route)
        }

        Spacer(modifier = Modifier.height(16.dp))

        GarageServiceCard(
            icon = { Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF16A34A)) },
            title = "Active Request",
            subtitle = "Track your ongoing garage request"
        ) {
            scope.launch {
                val rid = tokenStore.getLastGarageRequestId()
                if (rid.isNullOrBlank()) {
                    error = "No active garage request yet. Please send a request first."
                    return@launch
                }
                error = null
                navController.navigate(Routes.GarageActiveScreen.createRoute(rid))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        GarageServiceCard(
            icon = { Icon(Icons.Default.History, null, tint = Color(0xFF0284C7)) },
            title = "Garage History",
            subtitle = "View past garage requests"
        ) {
            navController.navigate(Routes.GarageHistoryScreen.route)
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
private fun GarageServiceCard(
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