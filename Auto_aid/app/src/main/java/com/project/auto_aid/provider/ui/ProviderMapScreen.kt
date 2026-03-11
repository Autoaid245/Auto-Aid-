package com.project.auto_aid.provider.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.project.auto_aid.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderMapScreen(
    requestId: String,
    pickupLat: Double,
    pickupLng: Double
) {
    val context = LocalContext.current

    // ✅ Provider GPS (real)
    var providerLocation by remember { mutableStateOf<LatLng?>(null) }

    // ✅ FIXED user pickup location (no state write during composition)
    val userLocation = remember(pickupLat, pickupLng) { LatLng(pickupLat, pickupLng) }

    // ✅ Polyline points
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // ✅ Route loading / error
    var routeLoading by remember { mutableStateOf(false) }
    var routeError by remember { mutableStateOf<String?>(null) }

    val fused = remember { LocationServices.getFusedLocationProviderClient(context) }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    // ✅ Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (hasLocationPermission()) {
            fused.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) providerLocation = LatLng(loc.latitude, loc.longitude)
            }
        }
    }

    // ✅ Get provider GPS once on open
    LaunchedEffect(Unit) {
        if (!hasLocationPermission()) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            fused.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) providerLocation = LatLng(loc.latitude, loc.longitude)
            }
        }
    }

    // ✅ Camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    // ============================
    // ✅ GOOGLE DIRECTIONS API CALL
    // ============================
    suspend fun fetchRoute(origin: LatLng, dest: LatLng): List<LatLng> {
        val key = BuildConfig.MAPS_API_KEY
        if (key.isBlank()) return emptyList()

        val url =
            "https://maps.googleapis.com/maps/api/directions/json" +
                    "?origin=${origin.latitude},${origin.longitude}" +
                    "&destination=${dest.latitude},${dest.longitude}" +
                    "&mode=driving" +
                    "&key=$key"

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val req = Request.Builder().url(url).build()
                val res = client.newCall(req).execute()

                val body = res.body?.string().orEmpty()
                if (!res.isSuccessful) return@withContext emptyList()

                val json = JSONObject(body)
                val status = json.optString("status")
                if (status != "OK") return@withContext emptyList()

                val routes = json.optJSONArray("routes") ?: return@withContext emptyList()
                if (routes.length() == 0) return@withContext emptyList()

                val overview = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")

                decodePolyline(overview)
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    // ✅ Fetch route when provider location exists
    LaunchedEffect(providerLocation, userLocation) {
        val p = providerLocation ?: return@LaunchedEffect

        routeLoading = true
        routeError = null
        routePoints = emptyList()

        val points = fetchRoute(p, userLocation)
        routePoints = points

        if (points.isEmpty()) {
            routeError = "Failed to load route (check API key / billing / internet)."
        }
        routeLoading = false
    }

    // ✅ Open Google Maps navigation
    fun openGoogleNavigation(dest: LatLng) {
        val uri = Uri.parse("google.navigation:q=${dest.latitude},${dest.longitude}&mode=d")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        runCatching { context.startActivity(intent) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Provider → User Route") })
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { openGoogleNavigation(userLocation) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("Navigate in Google Maps")
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (routeLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            routeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission()
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = hasLocationPermission()
                )
            ) {
                providerLocation?.let { p ->
                    Marker(
                        state = MarkerState(position = p),
                        title = "You (Provider)"
                    )
                }

                Marker(
                    state = MarkerState(position = userLocation),
                    title = "User Pickup"
                )

                if (routePoints.isNotEmpty()) {
                    Polyline(points = routePoints)
                }
            }
        }
    }
}

/** ✅ Polyline decoder (Google encoded polyline) */
private fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)
        lng += dlng

        poly.add(LatLng(lat / 1E5, lng / 1E5))
    }

    return poly
}