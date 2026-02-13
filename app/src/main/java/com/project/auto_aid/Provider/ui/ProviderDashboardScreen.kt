package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.pinnedScrollBehavior
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashboardScreen(
    navController: NavHostController
) {


    val vm = remember { ProviderViewModel() }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val providerId = auth.currentUser?.uid ?: return

    var provider by remember { mutableStateOf<Provider?>(null) }
    var loading by remember { mutableStateOf(true) }
    var tab by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    /* ---------------- LOAD PROVIDER PROFILE ---------------- */
    LaunchedEffect(providerId) {
        db.collection("users").document(providerId).get()
            .addOnSuccessListener { doc ->

                val providerType = doc.getString("providerType") ?: ""

                provider = Provider(
                    id = providerId,
                    name = doc.getString("name") ?: "",
                    phone = doc.getString("phone") ?: "",
                    providerType = providerType,
                    rating = doc.getDouble("rating") ?: 0.0,
                    profileImageUrl = doc.getString("profileImageUrl") ?: ""
                )

                vm.start(providerType, providerId)
                loading = false
            }
            .addOnFailureListener { loading = false }
    }

    val uploadImage = rememberProfileImagePicker(providerId) { newUrl ->
        provider = provider?.copy(profileImageUrl = newUrl)
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val safeProvider = provider ?: return

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("AutoAid Provider") },
                scrollBehavior = scrollBehavior,
                actions = {

                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }

                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {

                        DropdownMenuItem(
                            text = { Text("Edit Profile") },
                            onClick = {
                                showMenu = false
                                navController.navigate(Routes.EditProviderProfile.route)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Logout", color = MaterialTheme.colorScheme.error) },
                            onClick = {

                                showMenu = false

                                // 🔴 Set offline first
                                db.collection("users").document(providerId)
                                    .update("isOnline", false)
                                    .addOnCompleteListener {

                                        FirebaseAuth.getInstance().signOut()

                                        navController.navigate(Routes.LoginScreen.route) {
                                            popUpTo(Routes.ProviderDashboard.route) { inclusive = true }
                                        }
                                    }
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {

            ProviderProfileCard(
                provider = safeProvider,
                onOnlineChange = { isOnline ->
                    db.collection("users").document(providerId).update("isOnline", isOnline)
                },
                onEditProfile = { navController.navigate(Routes.EditProviderProfile.route) },
                onChangeProfileImage = { uploadImage() }
            )

            Spacer(Modifier.height(8.dp))

            ProviderStatsRow(
                pending = vm.pending.size,
                active = vm.ongoing.size,
                completed = vm.completed.size
            )

            Spacer(Modifier.height(8.dp))

            TabRow(selectedTabIndex = tab) {
                listOf("Requests", "Active", "History").forEachIndexed { index, title ->
                    Tab(selected = tab == index, onClick = { tab = index }, text = { Text(title) })
                }
            }

            Spacer(Modifier.height(8.dp))

            when (tab) {

                0 -> LazyColumn {
                    items(vm.pending) { request ->
                        ProviderRequestCard(
                            request = request,
                            onAccept = {
                                vm.accept(request.id, providerId)
                                navController.navigate(Routes.ProviderActiveJob.createRoute(request.id))
                            },
                            onNavigate = {
                                navController.navigate(Routes.ProviderActiveJob.createRoute(request.id))
                            }
                        )
                    }
                }

                1 -> {
                    val activeRequest = vm.ongoing.firstOrNull()
                    if (activeRequest != null) {
                        ProviderActiveJobScreen(activeRequest.id, navController)
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No active job")
                        }
                    }
                }

                2 -> LazyColumn {
                    items(vm.completed) { request ->
                        ProviderRequestCard(request = request, onAccept = {}, onNavigate = {})
                    }
                }
            }
        }
    }


}
