package com.project.auto_aid.provider.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.provider.ProviderViewModel

@Composable
fun ProviderActiveJobScreen(
    requestId: String,
    navController: NavHostController
) {
    val vm: ProviderViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Active Job",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Request ID: $requestId")

        Spacer(modifier = Modifier.height(16.dp))

        // 🔵 OPEN MAP
        Button(
            onClick = {
                navController.navigate(
                    Routes.ProviderMapScreen.createRoute(requestId)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Map")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔴 COMPLETE JOB
        Button(
            onClick = {
                vm.updateStatus(requestId, "completed")
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Job")
        }
    }
}