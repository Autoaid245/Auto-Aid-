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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.project.auto_aid.components.GpsLocationSearchField

/* =====================================================
   COLORS
===================================================== */
object AppColors {
    val background = Color(0xFFF9F9F9)
    val primary = Color(0xFF19ABd9)
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

val quickAccessData = listOf(
    QuickAccessItem(Icons.Filled.CarRepair, "Garage"),
    QuickAccessItem(Icons.Filled.LocalShipping, "Towing Track"),
    QuickAccessItem(Icons.Filled.LocalGasStation, "Fuel Delivery"),
    QuickAccessItem(Icons.Filled.MedicalServices, "Ambulance")
)

data class ServiceItem(val name: String, val location: String, val imageRes: Int)

object AppImages {
    val shell = R.drawable.shell_2
    val total = R.drawable.total_1
    val stabex = R.drawable.stabex_2
    val rubis = R.drawable.rubis_1
    val hass = R.drawable.hass_1
    val gazz = R.drawable.gazz_1
}

val featuredServices = listOf(
    ServiceItem("Shell", "Kyaliwajara, Wakiso", AppImages.shell),
    ServiceItem("Total Energies", "Kira Road, Kampala", AppImages.total),
    ServiceItem("Stabex", "Entebbe, Wakiso", AppImages.stabex),
    ServiceItem("Rubis", "Ntinda, Wakiso", AppImages.rubis),
    ServiceItem("Hass Energies", "Jinja Road", AppImages.hass),
    ServiceItem("Gazz Energies", "Gayaza Road", AppImages.gazz)
)

/* =====================================================
   HOME SCREEN
===================================================== */
@Composable
fun HomeScreen(navController: NavHostController) {

    val isPreview = LocalInspectionMode.current

    var userName by remember { mutableStateOf("User") }

    // ✅ Safe: don't touch Firebase in Preview
    val auth = remember { if (isPreview) null else FirebaseAuth.getInstance() }
    val db = remember { if (isPreview) null else FirebaseFirestore.getInstance() }
    val uid = auth?.currentUser?.uid

    LaunchedEffect(uid) {
        if (uid.isNullOrBlank() || db == null) return@LaunchedEffect

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                userName = doc.getString("name")?.trim().takeIf { !it.isNullOrEmpty() } ?: "User"
            }
            .addOnFailureListener {
                userName = "User"
            }
    }

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(AppColors.background)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            TopHeader(userName)
            SearchAndProfileBar()
            QuickAccessGrid(navController)
            ReferralCard()
            FeaturesSection()
            RecentsSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/* =====================================================
   TOP HEADER
===================================================== */
@Composable
fun TopHeader(userName: String) {

    // start hidden, then fade in
    var showName by remember { mutableStateOf(false) }

    LaunchedEffect(userName) {
        showName = false
        if (userName.isNotBlank()) {
            showName = true
        }
    }

    val nameAlpha by animateFloatAsState(
        targetValue = if (showName) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "nameAlpha"
    )

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
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .alpha(nameAlpha)
    )
}
/* =====================================================
   BOTTOM NAV
===================================================== */
@Composable
fun AppBottomNavigationBar(navController: NavHostController) {

    NavigationBar(containerColor = AppColors.primary) {

        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate(Routes.HomeScreen.route) },
            icon = { Icon(Icons.Default.Home, null, tint = Color.White) },
            label = { Text("Home", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Routes.SettingsScreen.route) },
            icon = { Icon(Icons.Default.Settings, null, tint = Color.White) },
            label = { Text("Settings", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
    }
}

/* =====================================================
   SEARCH + PROFILE
===================================================== */



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
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(12.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, AppColors.primary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, null,
                modifier = Modifier.size(36.dp), tint = AppColors.textPrimary)
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(item.title, fontSize = 13.sp, color = AppColors.textSecondary)
    }
}

/* =====================================================
   REFERRAL
===================================================== */
@Composable
fun ReferralCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    )
    {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .size(48.dp)

            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Refer a friend", fontWeight = FontWeight.Bold)
                Text("Earn rewards instantly", fontSize = 12.sp)
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
   FEATURES + RECENTS
===================================================== */
@Composable
fun FeaturesSection() {
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
        items(featuredServices) { ServiceCard(it) }
    }
}

@Composable
fun ServiceCard(item: ServiceItem) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .height(240.dp),
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
data class RecentItem(
    val service: String,
    val date: String,
    val status: String,
    val icon: ImageVector
)

val recentItems = listOf(
    RecentItem("Fuel Delivery", "Today • 2:30 PM", "Completed", Icons.Default.LocalGasStation),
    RecentItem("Towing Service", "Yesterday • 6:15 PM", "Completed", Icons.Default.LocalShipping),
    RecentItem("Garage Repair", "Mon • 10:00 AM", "Cancelled", Icons.Default.CarRepair),
    RecentItem("Ambulance Service", "Sun • 9:20 PM", "Completed", Icons.Default.MedicalServices
    )

)

@Composable
fun RecentsSection() {

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
        items(recentItems) { item ->
            RecentCard(item)
        }
    }
}

@Composable
fun RecentCard(item: RecentItem) {

    val statusColor = when (item.status) {
        "Completed" -> Color(0xFF16A34A)   // green
        "Cancelled" -> Color(0xFFDC2626)   // red
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

@Composable
fun SearchAndProfileBar() {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        GpsLocationSearchField(
            onSearchChange = { query ->
                println("Searching for: $query")
            }
        )
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