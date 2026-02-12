package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.provider.ProviderViewModel
import com.project.auto_aid.provider.model.Provider

@Composable
fun ProviderDashboardScreen(
    navController: NavHostController
) {

    /* ---------------- CORE DEPENDENCIES ---------------- */
    val vm = remember { ProviderViewModel() }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val providerId = auth.currentUser?.uid ?: return

    /* ---------------- STATE ---------------- */
    var provider by remember { mutableStateOf<Provider?>(null) }
    var loading by remember { mutableStateOf(true) }
    var tab by remember { mutableStateOf(0) }

    /* ---------------- LOAD PROVIDER PROFILE ---------------- */
    LaunchedEffect(providerId) {
        db.collection("users")
            .document(providerId)
            .get()
            .addOnSuccessListener { doc ->

                val providerType = doc.getString("providerType") ?: ""

                provider = Provider(
                    id = providerId,
                    name = doc.getString("name") ?: "",
                    phone = doc.getString("phone") ?: "",
                    providerType = providerType,
                    rating = doc.getDouble("rating") ?: 0.0
                )

                // 🔥 START LISTENING USING REAL SERVICE TYPE
                vm.start(providerType, providerId)

                loading = false
            }
            .addOnFailureListener {
                loading = false
            }
    }

    /* ---------------- LOADING STATE ---------------- */
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val safeProvider = provider ?: return

    /* ---------------- UI ---------------- */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        /* ---------- PROFILE ---------- */
        ProviderProfileCard(safeProvider)

        Spacer(modifier = Modifier.height(8.dp))

        /* ---------- STATS ---------- */
        ProviderStatsRow(
            pending = vm.pending.size,
            active = vm.ongoing.size,
            completed = vm.completed.size
        )

        Spacer(modifier = Modifier.height(8.dp))

        /* ---------- TABS ---------- */
        TabRow(selectedTabIndex = tab) {
            listOf("Requests", "Active", "History").forEachIndexed { index, title ->
                Tab(
                    selected = tab == index,
                    onClick = { tab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        /* ---------- TAB CONTENT ---------- */
        when (tab) {

            /* ===== REQUESTS ===== */
            0 -> LazyColumn {
                items(vm.pending) { request ->
                    ProviderRequestCard(
                        request = request,
                        onAccept = {
                            vm.accept(request.id, providerId)
                            navController.navigate(
                                Routes.ProviderActiveJob.createRoute(request.id)
                            )
                        },
                        onNavigate = {
                            navController.navigate(
                                Routes.ProviderActiveJob.createRoute(request.id)
                            )
                        }
                    )
                }
            }

            /* ===== ACTIVE JOB ===== */
            1 -> {
                val activeRequest = vm.ongoing.firstOrNull()
                if (activeRequest != null) {
                    ProviderActiveJobScreen(
                        requestId = activeRequest.id,
                        navController = navController
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active job")
                    }
                }
            }

            /* ===== HISTORY ===== */
            2 -> LazyColumn {
                items(vm.completed) { request ->
                    ProviderRequestCard(
                        request = request,
                        onAccept = {},
                        onNavigate = {}
                    )
                }
            }
        }
    }
}