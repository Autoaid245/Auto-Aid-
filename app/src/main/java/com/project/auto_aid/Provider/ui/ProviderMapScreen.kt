package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.project.auto_aid.provider.ProviderViewModel

@Composable
fun ProviderMapScreen(
    requestId: String,
    confirmProvider: Boolean
) {
    // ✅ ViewModel obtained correctly
    val vm: ProviderViewModel = viewModel()

    // 🔵 Provider location (TEMP – replace with real GPS later)
    val providerLocation = remember {
        LatLng(0.3476, 32.5825) // Kampala sample
    }

    // 🔴 User live location
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // 🔹 Listen to USER location in real time
    LaunchedEffect(requestId) {
        vm.listenUserLocation(requestId) { lat, lng ->
            userLocation = LatLng(lat, lng)
        }
    }

    // 🔹 Camera state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            providerLocation,
            14f
        )
    }

    // 🔥 Move camera when user location updates
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f),
                durationMs = 1000
            )
        }
    }

    // 🔹 Google Map
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = confirmProvider),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = confirmProvider
        )
    ) {

        // 🔵 Provider Marker
        Marker(
            state = MarkerState(position = providerLocation),
            title = "You (Provider)"
        )

        // 🔴 User Marker
        userLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "User"
            )
        }
    }
}