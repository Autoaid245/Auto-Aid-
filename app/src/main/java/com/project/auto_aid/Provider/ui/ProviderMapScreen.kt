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
    requestId: String
) {

    val vm: ProviderViewModel = viewModel()

    // 🔵 Provider location (Temporary – replace with real GPS later)
    val providerLocation = remember {
        LatLng(0.3476, 32.5825) // Kampala sample
    }

    // 🔴 User live location
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // 🔹 Listen to USER location
    LaunchedEffect(requestId) {
        vm.listenUserLocation(requestId) { lat, lng ->
            userLocation = LatLng(lat, lng)
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            providerLocation,
            14f
        )
    }

    // 🔥 Animate camera when user moves
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 15f),
                durationMs = 1000
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true   // Always enabled for provider
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = true
        )
    ) {

        // 🔵 Provider marker
        Marker(
            state = MarkerState(position = providerLocation),
            title = "You (Provider)"
        )

        // 🔴 User marker
        userLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "User"
            )
        }
    }
}