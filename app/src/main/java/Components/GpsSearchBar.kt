package com.project.auto_aid.components

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsLocationSearchField(
    modifier: Modifier = Modifier,
    onSearchChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fine || coarse) {
            scope.launch {
                val place = getCurrentPlaceName(context)
                if (!place.isNullOrBlank()) {
                    searchQuery = place
                    onSearchChange(place)
                } else {
                    Toast.makeText(
                        context,
                        "Could not get location. Turn on GPS.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearchChange(it)
        },
        placeholder = { Text("Search by location") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            IconButton(
                onClick = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Use my location")
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

private suspend fun getCurrentPlaceName(context: Context): String? {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    return try {
        val location = getLastLocationSafe(fusedClient) ?: return null

        withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())

            val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            } else {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }

            val addr = addresses?.firstOrNull()
            val city = addr?.locality ?: addr?.subAdminArea
            val country = addr?.countryName

            when {
                !city.isNullOrBlank() && !country.isNullOrBlank() -> "$city, $country"
                !country.isNullOrBlank() -> country
                else -> addr?.getAddressLine(0)
            }
        }
    } catch (_: Exception) {
        null
    }
}

private suspend fun getLastLocationSafe(
    fusedClient: FusedLocationProviderClient
): android.location.Location? {
    return suspendCancellableCoroutine { cont ->
        // ✅ IMPORTANT: use Java method getLastLocation() (prevents the red underline on lastLocation)
        fusedClient.getLastLocation()
            .addOnSuccessListener { location ->
                if (cont.isActive) cont.resume(location)
            }
            .addOnFailureListener {
                if (cont.isActive) cont.resume(null)
            }
    }
}