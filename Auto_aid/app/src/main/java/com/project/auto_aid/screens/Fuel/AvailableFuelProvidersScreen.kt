package com.project.auto_aid.screens.fuel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.data.network.RetrofitClient
import com.project.auto_aid.data.network.dto.ProviderLiteDto
import com.project.auto_aid.navigation.Routes

data class FuelProviderLite(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val providerType: String = "",
    val rating: Double = 0.0,
    val profileImageUrl: String = "",
    val isOnline: Boolean = false
)

private fun ProviderLiteDto.toFuelUi(): FuelProviderLite {
    return FuelProviderLite(
        id = id ?: "",
        name = name ?: "",
        phone = phone ?: "",
        providerType = providerType ?: "",
        rating = rating ?: 0.0,
        profileImageUrl = profileImageUrl ?: "",
        isOnline = isOnline ?: false
    )
}

@Composable
fun AvailableFuelProvidersScreen(navController: NavHostController) {
    val context = LocalContext.current
    val api = remember(context) { RetrofitClient.create(TokenStore(context)) }

    var loading by remember { mutableStateOf(true) }
    var providers by remember { mutableStateOf<List<FuelProviderLite>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        loading = true
        error = null

        runCatching {
            val response = api.getAvailableProviders(providerType = "fuel", isOnline = true)

            if (!response.isSuccessful) {
                throw Exception("HTTP ${response.code()}")
            }

            val body = response.body() ?: emptyList()
            body.map { dto -> dto.toFuelUi() }
        }.onSuccess { list ->
            providers = list
        }.onFailure { e ->
            error = e.message ?: "Failed to load providers"
            providers = emptyList()
        }

        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Available Fuel Providers",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text("Choose a provider to send your request.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error ?: "Error")
            }
            return
        }

        if (providers.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No fuel providers online right now.")
            }
            return
        }

        LazyColumn {
            items(providers) { p ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate(
                                Routes.FuelRequestScreen.createRoute(providerId = p.id)
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(p.name, fontWeight = FontWeight.Bold)
                            Text("⭐ ${"%.1f".format(p.rating)}")
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("Phone: ${p.phone}", style = MaterialTheme.typography.bodySmall)
                        Text("Tap to request", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}