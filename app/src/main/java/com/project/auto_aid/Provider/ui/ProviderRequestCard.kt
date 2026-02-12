package com.project.auto_aid.provider.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.project.auto_aid.provider.model.Request

@Composable
fun ProviderRequestCard(
    request: Request,
    onAccept: () -> Unit,
    onNavigate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("Status: ${request.status}")
            Spacer(Modifier.height(8.dp))

            when (request.status) {
                "pending" -> Button(onClick = onAccept) { Text("Accept") }
                else -> Button(onClick = onNavigate) { Text("Open Job") }
            }
        }
    }
}