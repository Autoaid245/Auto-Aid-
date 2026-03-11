package com.project.auto_aid.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.R
import com.project.auto_aid.components.GpsLocationSearchField
import com.project.auto_aid.data.local.TokenStore
import com.project.auto_aid.data.network.RetrofitClient
import com.project.auto_aid.navigation.Routes

/* =====================================================
   COLORS
===================================================== */
object AppColors {
    val background = Color(0xFFF9F9F9)
    val primary = Color(0xFF19ABD9)
    val secondary = Color(0xFFE5E7EB)
    val textPrimary = Color(0xFF374151)
    val textSecondary = Color(0xFF4B5563)
    val referralCardBackground = Color(0xFFEDE9FE)
    val referralCardIcon = Color(0xFF7C3AED)
}

/* =====================================================
   DATA MODELS
===================================================== */
data class QuickAccessItem(val icon: ImageVector, val title: String)

data class ServiceItem(val name: String, val location: String, val imageRes: Int)

data class RecentItem(
    val service: String,
    val date: String,
    val status: String,
    val icon: ImageVector
)

data class ReferralUiData(
    val title: String = "Refer a friend",
    val subtitle: String = "Earn rewards instantly",
    val code: String = "AUTOAID",
    val earnedAmount: String = "UGX 0"
)

/* =====================================================
   STATIC FALLBACKS / DEFAULTS
===================================================== */
val quickAccessData = listOf(
    QuickAccessItem(Icons.Filled.CarRepair, "Garage"),
    QuickAccessItem(Icons.Filled.LocalShipping, "Towing Track"),
    QuickAccessItem(Icons.Filled.LocalGasStation, "Fuel Delivery"),
    QuickAccessItem(Icons.Filled.MedicalServices, "Ambulance")
)

object AppImages {
    val shell = R.drawable.shell_2
    val total = R.drawable.total_1
    val stabex = R.drawable.stabex_2
    val rubis = R.drawable.rubis_1
    val hass = R.drawable.hass_1
    val gazz = R.drawable.gazz_1
}

val defaultFeaturedServices = listOf(
    ServiceItem("Shell", "Kyaliwajara, Wakiso", AppImages.shell),
    ServiceItem("Total Energies", "Kira Road, Kampala", AppImages.total),
    ServiceItem("Stabex", "Entebbe, Wakiso", AppImages.stabex),
    ServiceItem("Rubis", "Ntinda, Wakiso", AppImages.rubis),
    ServiceItem("Hass Energies", "Jinja Road", AppImages.hass),
    ServiceItem("Gazz Energies", "Gayaza Road", AppImages.gazz)
)

val defaultRecentItems = listOf(
    RecentItem("Fuel Delivery", "Today • 2:30 PM", "Completed", Icons.Default.LocalGasStation),
    RecentItem("Towing Service", "Yesterday • 6:15 PM", "Completed", Icons.Default.LocalShipping),
    RecentItem("Garage Repair", "Mon • 10:00 AM", "Cancelled", Icons.Default.CarRepair),
    RecentItem("Ambulance Service", "Sun • 9:20 PM", "Completed", Icons.Default.MedicalServices)
)

/* =====================================================
   HOME SCREEN
===================================================== */
@Composable
fun HomeScreen(navController: NavHostController) {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    val tokenStore = remember(context) { TokenStore(context) }
    val api = remember(tokenStore) { RetrofitClient.create(tokenStore) }

    var userName by rememberSaveable { mutableStateOf("User") }
    var notificationCount by rememberSaveable { mutableIntStateOf(0) }

    var featuredServicesState by remember {
        mutableStateOf(defaultFeaturedServices)
    }

    var recentItemsState by remember {
        mutableStateOf(defaultRecentItems)
    }

    var referralState by remember {
        mutableStateOf(
            ReferralUiData(
                title = "Refer a friend",
                subtitle = "Earn rewards instantly",
                code = "AUTOAID",
                earnedAmount = "UGX 0"
            )
        )
    }

    var isLoadingHome by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect

        isLoadingHome = true

        // USER NAME
        runCatching {
            val res = api.getMe()
            if (res.isSuccessful) {
                val user = res.body()?.user
                userName = user?.name?.trim().takeIf { !it.isNullOrEmpty() } ?: "User"
            } else {
                userName = "User"
            }
        }.onFailure {
            userName = "User"
        }

        // NOTIFICATIONS
        // Replace with real backend endpoint later
        runCatching {
            notificationCount = 3
        }.onFailure {
            notificationCount = 0
        }

        // FEATURED SERVICES
        // Replace with real backend providers endpoint later
        runCatching {
            featuredServicesState = defaultFeaturedServices
        }.onFailure {
            featuredServicesState = defaultFeaturedServices
        }

        // RECENTS
        // Replace with real backend request history later
        runCatching {
            recentItemsState = defaultRecentItems
        }.onFailure {
            recentItemsState = defaultRecentItems
        }

        // REFERRAL
        // Replace with real backend referral endpoint later
        runCatching {
            referralState = ReferralUiData(
                title = "Refer a friend",
                subtitle = "Earn rewards instantly",
                code = "AUTOAID",
                earnedAmount = "UGX 0"
            )
        }.onFailure {
            referralState = ReferralUiData()
        }

        isLoadingHome = false
    }

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                notificationCount = notificationCount
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(AppColors.background)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            TopHeader(userName = userName)

            SearchAndProfileBar(navController)
            QuickAccessGrid(navController)
            ReferralCard(referralState)
            FeaturesSection(featuredServicesState)
            RecentsSection(recentItemsState)

            if (isLoadingHome) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/* =====================================================
   TOP HEADER
===================================================== */
@Composable
fun TopHeader(userName: String) {
    var showName by remember { mutableStateOf(false) }

    LaunchedEffect(userName) {
        showName = userName.isNotBlank()
    }

    val nameAlpha by animateFloatAsState(
        targetValue = if (showName) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "nameAlpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                append("Hello, ")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(userName)
                }
            },
            fontSize = 18.sp,
            modifier = Modifier.alpha(nameAlpha)
        )
    }
}

/* =====================================================
   BOTTOM NAV
===================================================== */
@Composable
fun AppBottomNavigationBar(
    navController: NavHostController,
    notificationCount: Int
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    fun navigateSingleTop(route: String) {
        if (currentRoute == route) return
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(Routes.HomeScreen.route) {
                saveState = true
            }
        }
    }

    NavigationBar(containerColor = AppColors.primary) {

        NavigationBarItem(
            selected = currentRoute == Routes.HomeScreen.route,
            onClick = { navigateSingleTop(Routes.HomeScreen.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Home", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )

        NavigationBarItem(
            selected = currentRoute == Routes.NotificationScreen.route,
            onClick = { navigateSingleTop(Routes.NotificationScreen.route) },
            icon = {
                Box(
                    modifier = Modifier.size(26.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )

                    if (notificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            label = { Text("Alerts", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )

        NavigationBarItem(
            selected = currentRoute == Routes.SettingsScreen.route,
            onClick = { navigateSingleTop(Routes.SettingsScreen.route) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White) },
            label = { Text("Settings", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
    }
}

/* =====================================================
   SEARCH + PROFILE
===================================================== */
@Composable
fun SearchAndProfileBar(navController: NavHostController) {
    var locationText by remember { mutableStateOf("") }

    val saved = navController.currentBackStackEntry?.savedStateHandle

    val labelFlow = saved?.getStateFlow("picked_location_label", "")?.collectAsState()
    val latFlow = saved?.getStateFlow("picked_location_lat", 0.0)?.collectAsState()
    val lngFlow = saved?.getStateFlow("picked_location_lng", 0.0)?.collectAsState()

    LaunchedEffect(labelFlow?.value) {
        val label = labelFlow?.value.orEmpty()
        if (label.isNotBlank()) locationText = label
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        GpsLocationSearchField(
            value = locationText,
            onValueChange = { locationText = it },
            onOpenMapPicker = { lat, lng ->
                navController.navigate(
                    Routes.LocationPicker.createRoute(lat, lng)
                )
            }
        )

        if (!labelFlow?.value.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Picked: ${labelFlow?.value}  (${latFlow?.value}, ${lngFlow?.value})",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/* =====================================================
   QUICK ACCESS
===================================================== */
@Composable
fun QuickAccessGrid(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        quickAccessData.forEach { item ->
            QuickAccessItemView(item, Modifier.weight(1f), navController)
        }
    }
}

@Composable
fun QuickAccessItemView(
    item: QuickAccessItem,
    modifier: Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable {
                when (item.title) {
                    "Garage" -> navController.navigate(Routes.GarageScreen.route)
                    "Towing Track" -> navController.navigate(Routes.TowingScreen.route)
                    "Fuel Delivery" -> navController.navigate(Routes.FuelScreen.route)
                    "Ambulance" -> navController.navigate(Routes.AmbulanceScreen.route)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp), clip = false)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, AppColors.primary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, null, modifier = Modifier.size(36.dp), tint = AppColors.textPrimary)
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(item.title, fontSize = 13.sp, color = AppColors.textSecondary)
    }
}

/* =====================================================
   REFERRAL
===================================================== */
@Composable
fun ReferralCard(referral: ReferralUiData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(referral.title, fontWeight = FontWeight.Bold)
                Text(referral.subtitle, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Code: ${referral.code}",
                    fontSize = 12.sp,
                    color = AppColors.textSecondary
                )
                Text(
                    "Earned: ${referral.earnedAmount}",
                    fontSize = 12.sp,
                    color = AppColors.textSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Refer")
                }
            }
        }
    }
}

/* =====================================================
   FEATURES
===================================================== */
@Composable
fun FeaturesSection(items: List<ServiceItem>) {
    Text(
        "Featured Services",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(20.dp),
        color = AppColors.textPrimary
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { ServiceCard(it) }
    }
}

@Composable
fun ServiceCard(item: ServiceItem) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(240.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp), clip = false),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(12.dp)
            ) {
                Text(item.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(item.location, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

/* =====================================================
   RECENTS
===================================================== */
@Composable
fun RecentsSection(items: List<RecentItem>) {
    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = "Recents",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = AppColors.textPrimary,
        modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            RecentCard(item)
        }
    }
}

@Composable
fun RecentCard(item: RecentItem) {
    val statusColor = when (item.status) {
        "Completed" -> Color(0xFF16A34A)
        "Cancelled" -> Color(0xFFDC2626)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .width(230.dp)
            .height(130.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(AppColors.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = AppColors.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.service,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Text(
                        text = item.date,
                        fontSize = 12.sp,
                        color = AppColors.textSecondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.status,
                        fontSize = 12.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/* =====================================================
   PREVIEW
===================================================== */
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController())
}