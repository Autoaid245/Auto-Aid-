package com.project.auto_aid.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

/* =====================================================
   COLORS
===================================================== */
object AppColors {
    val background = Color(0xFFF9F9F9)
    val primary = Color(0xFF0895DD)
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
    QuickAccessItem(Icons.Default.Build, "Garage"),
    QuickAccessItem(Icons.Default.Warning, "Towing Track"),
    QuickAccessItem(Icons.Default.Phone, "Fuel Delivery"),
    QuickAccessItem(Icons.Default.Call, "Ambulance")
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
fun HomeScreen(
    navController: NavHostController,
    userName: String = "User"
) {

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
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, AppColors.secondary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, null, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(item.title, fontSize = 13.sp, color = AppColors.textSecondary)
    }
}

/* =====================================================
   HEADER + SEARCH
===================================================== */
@Composable
fun TopHeader(userName: String) {
    Text(
        text = "Hello, $userName",
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = AppColors.textPrimary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
fun SearchAndProfileBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search by location") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AppColors.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Color.White)
        }
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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AppColors.referralCardIcon,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Refer a friend", fontWeight = FontWeight.Bold)
                Text("Earn rewards instantly", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {}) {
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
        modifier = Modifier.padding(20.dp)
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

@Composable
fun RecentsSection() {
    Spacer(modifier = Modifier.height(20.dp))
    Text("Recents", fontWeight = FontWeight.Bold, modifier = Modifier.padding(20.dp))
}

/* =====================================================
   PREVIEW
===================================================== */
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(rememberNavController(), "Dave")
}