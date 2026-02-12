package com.project.auto_aid.provider.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import com.project.auto_aid.provider.model.Provider

@Composable
fun ProviderProfileCard(provider: Provider) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(provider.name, style = MaterialTheme.typography.titleLarge)
            Text("📞 ${provider.phone}")
            Text("⭐ ${provider.rating}")
            Text("Service: ${provider.providerType}")
        }
    }
}