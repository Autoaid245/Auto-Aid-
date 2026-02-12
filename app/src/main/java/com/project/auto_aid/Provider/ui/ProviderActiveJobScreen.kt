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
        Text("Active Job", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Request ID: $requestId")

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(8.dp))

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