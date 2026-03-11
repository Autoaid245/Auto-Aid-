package com.project.auto_aid.provider.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.data.network.RetrofitClient
import com.project.auto_aid.data.network.dto.ProviderDto
import com.project.auto_aid.data.network.dto.UpdateProfileRequest
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.provider.ProviderViewModel
import com.project.auto_aid.provider.model.Provider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDashboardScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val vm: ProviderViewModel = viewModel()
    val api = remember { RetrofitClient.create(TokenStore(context)) }

    var provider by remember { mutableStateOf<Provider?>(null) }
    var providerId by remember { mutableStateOf("") }
    var providerType by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    var tab by remember { mutableIntStateOf(1) } // Active selected like screenshot
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var startedListening by remember { mutableStateOf(false) }

    // UI toggle only (you can wire real theme later)
    var isDark by remember { mutableStateOf(false) }

    /* ---------------- LOAD PROVIDER PROFILE ---------------- */
    LaunchedEffect(Unit) {
        loading = true
        errorMsg = null
        try {
            val res = api.getProviderMe()

            if (!res.isSuccessful) {
                val err = res.errorBody()?.string()
                errorMsg = when (res.code()) {
                    401 -> "Session expired. Please login again."
                    403 -> "Access denied."
                    else -> err ?: "Failed to load profile (${res.code()})"
                }
                provider = null

                if (res.code() == 401) {
                    TokenStore(context).saveToken("")
                    navController.navigate(Routes.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                return@LaunchedEffect
            }

            val u: ProviderDto? = res.body()
            if (u == null) {
                errorMsg = "Empty profile response"
                provider = null
                return@LaunchedEffect
            }

            if ((u.role ?: "").lowercase() != "provider") {
                errorMsg = "Not a provider account"
                provider = null
                return@LaunchedEffect
            }

            providerId = u.resolvedId()
            providerType = u.providerType ?: u.businessType ?: ""

            provider = Provider(
                id = providerId,
                name = u.name ?: "",
                phone = u.phone ?: "",
                providerType = providerType,
                rating = u.rating ?: 0.0,
                profileImageUrl = u.profileImageUrl ?: u.logoUrl ?: ""
            )
        } catch (e: Exception) {
            errorMsg = e.message ?: "Failed to load provider profile"
            provider = null
        } finally {
            loading = false
        }
    }

    /* ✅ START LISTENING ONCE */
    LaunchedEffect(providerType, providerId) {
        if (!startedListening && providerType.isNotBlank() && providerId.isNotBlank()) {
            startedListening = true
            vm.start(providerType, providerId)
        }
    }

    /* ---------------- IMAGE UPLOAD HANDLER ---------------- */
    val uploadImage = rememberProfileImagePicker { newUrl ->
        provider = provider?.copy(profileImageUrl = newUrl)
        scope.launch {
            runCatching {
                api.updateProviderProfile(
                    UpdateProfileRequest(
                        name = provider?.name ?: "",
                        phone = provider?.phone ?: "",
                        profileImageUrl = newUrl
                    )
                )
            }
        }
    }

    /* ---------------- LOADING/ERROR ---------------- */
    if (loading) {
        Scaffold(
            topBar = {
                DashboardTopBar(
                    isDark = isDark,
                    onToggleDark = { isDark = !isDark },
                    onBell = { navController.navigate(Routes.ProviderNotifications.route) }
                )
            },
            bottomBar = {
                ProviderBottomNav(navController = navController, selectedRoute = Routes.ProviderDashboard.route)
            }
        ) { inner ->
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    if (provider == null) {
        Scaffold(
            topBar = {
                DashboardTopBar(
                    isDark = isDark,
                    onToggleDark = { isDark = !isDark },
                    onBell = { navController.navigate(Routes.ProviderNotifications.route) }
                )
            },
            bottomBar = {
                ProviderBottomNav(navController = navController, selectedRoute = Routes.ProviderDashboard.route)
            }
        ) { inner ->
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(errorMsg ?: "Provider profile not found", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = {
                            startedListening = false
                            navController.navigate(Routes.ProviderDashboard.route) {
                                popUpTo(Routes.ProviderDashboard.route) { inclusive = true }
                            }
                        }) { Text("Retry") }
                    }
                }
            }
        }
        return
    }

    val safeProvider = provider!!

    /* ---------------- UI ---------------- */
    Scaffold(
        topBar = {
            DashboardTopBar(
                isDark = isDark,
                onToggleDark = { isDark = !isDark },
                onBell = { navController.navigate(Routes.ProviderNotifications.route) }
            )
        },
        bottomBar = {
            ProviderBottomNav(navController = navController, selectedRoute = Routes.ProviderDashboard.route)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {

            // ✅ Big rounded container like screenshot
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(14.dp)) {

                    ProviderProfileCard(
                        provider = safeProvider,
                        onOnlineChange = { isOnline ->
                            scope.launch {
                                runCatching {
                                    api.updateProviderProfile(
                                        UpdateProfileRequest(
                                            name = safeProvider.name,
                                            phone = safeProvider.phone,
                                            isOnline = isOnline
                                        )
                                    )
                                }
                            }
                        },
                        onEditProfile = { navController.navigate(Routes.EditProviderProfile.route) },
                        onChangeProfileImage = { uploadImage() }
                    )

                    Spacer(Modifier.height(12.dp))

                    ProviderStatsRow(
                        pending = vm.pending.size,
                        active = vm.ongoing.size,
                        completed = vm.completed.size
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            DashboardTabs(selected = tab, onSelect = { tab = it })

            Spacer(Modifier.height(10.dp))

            when (tab) {
                0 -> DashboardList(
                    emptyText = "No pending requests",
                    items = vm.pending,
                    contentPadding = PaddingValues(bottom = 22.dp)
                ) { request ->
                    ProviderRequestCard(
                        request = request,
                        onView = {
                            navController.navigate(Routes.ProviderRequestDetails.createRoute(request.id))
                        },
                        onAccept = {
                            scope.launch {
                                vm.accept(request.id)
                                navController.navigate(Routes.ProviderActiveJob.createRoute(request.id))
                            }
                        },
                        onDecline = { scope.launch { vm.decline(request.id) } }
                    )
                }

                1 -> {
                    if (vm.ongoing.isEmpty()) {
                        EmptyJobsState(
                            title = "No active jobs",
                            subtitle = "When you accept a job, it will appear here."
                        )
                    } else {
                        DashboardList(
                            emptyText = "No active jobs",
                            items = vm.ongoing,
                            contentPadding = PaddingValues(bottom = 22.dp)
                        ) { request ->
                            ProviderRequestCard(
                                request = request,
                                onAccept = {},
                                onDecline = {},
                                onView = {
                                    navController.navigate(Routes.ProviderActiveJob.createRoute(request.id))
                                }
                            )
                        }
                    }
                }

                2 -> DashboardList(
                    emptyText = "No history yet",
                    items = vm.completed,
                    contentPadding = PaddingValues(bottom = 22.dp)
                ) { request ->
                    ProviderRequestCard(
                        request = request,
                        onAccept = {},
                        onDecline = {},
                        onView = {
                            navController.navigate(Routes.ProviderRequestDetails.createRoute(request.id))
                        }
                    )
                }
            }
        }
    }
}

/* ---------------- TOP BAR (bell + dark toggle) ---------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    isDark: Boolean,
    onToggleDark: () -> Unit,
    onBell: () -> Unit
) {
    TopAppBar(
        title = { Text("Dashboard", fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = onBell) {
                Icon(Icons.Outlined.NotificationsNone, contentDescription = "Notifications")
            }
            IconButton(onClick = onToggleDark) {
                Icon(
                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle theme"
                )
            }
        }
    )
}

/* ---------------- Tabs ---------------- */
@Composable
private fun DashboardTabs(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selected, modifier = Modifier.fillMaxWidth()) {
        listOf("Requests", "Active", "History").forEachIndexed { i, t ->
            Tab(
                selected = selected == i,
                onClick = { onSelect(i) },
                text = {
                    Text(
                        t,
                        fontWeight = if (selected == i) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}

/* ---------------- Empty state ---------------- */
@Composable
private fun EmptyJobsState(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(6.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/* ---------------- List helper ---------------- */
@Composable
private fun DashboardList(
    emptyText: String,
    items: List<com.project.auto_aid.provider.model.ProviderRequest>,
    contentPadding: PaddingValues,
    row: @Composable (com.project.auto_aid.provider.model.ProviderRequest) -> Unit
) {
    if (items.isEmpty()) {
        Card(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(emptyText, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.id }) { item ->
                row(item)
            }
        }
    }
}

/* ---------------- Bottom Nav (NO PLUS) ---------------- */
@Composable
private fun ProviderBottomNav(
    navController: NavHostController,
    selectedRoute: String
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedRoute == Routes.ProviderDashboard.route,
            onClick = {
                navController.navigate(Routes.ProviderDashboard.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") }
        )

        NavigationBarItem(
            selected = selectedRoute == Routes.ProviderMapHome.route,
            onClick = {
                navController.navigate(Routes.ProviderMapHome.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Outlined.Map, contentDescription = "Map") },
            label = { Text("Map") }
        )

        NavigationBarItem(
            selected = selectedRoute == Routes.ProviderChatList.route,
            onClick = {
                navController.navigate(Routes.ProviderChatList.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chat") },
            label = { Text("Chat") }
        )

        NavigationBarItem(
            selected = selectedRoute == Routes.ProviderProfile.route,
            onClick = {
                navController.navigate(Routes.ProviderProfile.route) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}