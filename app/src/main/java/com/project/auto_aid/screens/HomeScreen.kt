package com.project.auto_aid.screens

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

// ======================================================
// COLORS
// ======================================================
object AppColors {
    val background = Color(0xFFF9F9F9)
    val primary = Color(0xFF0895DD)
    val secondary = Color(0xFFE5E7EB)
    val textPrimary = Color(0xFF374151)
    val textSecondary = Color(0xFF4B5563)
    val referralCardBackground = Color(0xFFEDE9FE)
    val referralCardIcon = Color(0xFF7C3AED)
}

// ======================================================
// DATA MODELS
// ======================================================
data class QuickAccessItem(val icon: ImageVector, val title: String)

val quickAccessData = listOf(
    QuickAccessItem(Icons.Default.Garage, "Garage"),
    QuickAccessItem(Icons.Default.CarCrash, "Towing Track"),
    QuickAccessItem(Icons.Default.LocalGasStation, "Fuel Delivery"),
    QuickAccessItem(Icons.Default.LocalCarWash, "Ambulance")
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

// ======================================================
// HOME SCREEN
// ======================================================
@Composable
fun HomeScreen(navController: NavHostController, userName: String = "User") {

    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppColors.background)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(4.dp))

            TopHeader(userName)
            SearchAndProfileBar()
            QuickAccessGrid()
            ReferralCard()
            FeaturesSection()
            RecentsSection()

            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

// ======================================================
// HEADER
// ======================================================
@Composable
fun TopHeader(userName: String) {
    Text(
        text = userName,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.textPrimary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

// ======================================================
// SEARCH BAR + PROFILE ICON (NO NAVIGATION HERE ANYMORE)
// ======================================================
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
            placeholder = { Text("Search by Zip Code") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEDEDED),
                unfocusedContainerColor = Color(0xFFEDEDED),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AppColors.primary
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(AppColors.primary)
                .clickable { /* NO PROFILE NAV HERE ANYMORE */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

// ======================================================
// QUICK ACCESS
// ======================================================
@Composable
fun QuickAccessGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        quickAccessData.forEach { item ->
            QuickAccessItemView(item, Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickAccessItemView(item: QuickAccessItem, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable {},
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
            Icon(
                item.icon,
                contentDescription = item.title,
                tint = AppColors.textPrimary,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(item.title, fontSize = 13.sp, color = AppColors.textSecondary)
    }
}

// ======================================================
// REFERRAL CARD
// ======================================================
@Composable
fun ReferralCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.referralCardBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = AppColors.referralCardIcon,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("Refer A Friend And Get Reward!", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Invite a friend and win prizes.", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary)
                ) {
                    Text("Refer", fontSize = 13.sp)
                }
            }
        }
    }
}

// ======================================================
// FEATURES SECTION
// ======================================================
@Composable
fun FeaturesSection() {
    Column {
        Text(
            "Features",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(featuredServices) { item -> ServiceCard(item) }
        }
    }
}

@Composable
fun ServiceCard(item: ServiceItem) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(220.dp)
            .height(250.dp)
            .clickable {}
    ) {
        Box(Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(12.dp)
            ) {
                Text(item.name, color = Color.White, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null,
                        tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.location, color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// ======================================================
// RECENTS SECTION
// ======================================================
@Composable
fun RecentsSection() {
    Spacer(modifier = Modifier.height(22.dp))

    Column {
        Text(
            "Recents",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { RecentsCard("Just added", Color(0xFF0B90D5)) }
            item { RecentsCard("Booked", Color(0xFF10B981)) }
            item { RecentsCard("Completed", Color(0xFFFBBF24)) }
        }
    }
}

@Composable
fun RecentsCard(label: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(220.dp)
            .height(120.dp)
            .clickable {}
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF1F2937))
        ) {
            Box(Modifier.fillMaxSize().background(color.copy(alpha = 0.6f)))

            Text(
                label,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// ======================================================
// BOTTOM NAV BAR — NOW SETTINGS OPENS PROFILE SCREEN
// ======================================================
@Composable
fun AppBottomNavigationBar(navController: NavHostController) {

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    val navItems = listOf(
        Triple(Icons.Default.Home, "Home", Routes.HomeScreen.route),
        Triple(Icons.Default.FavoriteBorder, "Favourite", "favourite_screen"),
        Triple(Icons.Default.ChatBubbleOutline, "Chat", "chat_screen"),
        Triple(Icons.Default.Settings, "Settings", Routes.SettingsScreen.route)
    )

    // Animation target index
    val selectedIndex = navItems.indexOfFirst { it.third == currentRoute }
    val underlineOffset by animateDpAsState(
        targetValue = (selectedIndex.takeIf { it >= 0 } ?: 0) * 115.dp,
        label = "underline_offset"
    )

    Box {
        NavigationBar(
            containerColor = AppColors.primary,
            tonalElevation = 4.dp
        ) {

            navItems.forEachIndexed { index, (icon, label, route) ->

                val isSelected = currentRoute == route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(route) { inclusive = false }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = Color.White
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        unselectedIconColor = Color(0xCCFFFFFF),
                        unselectedTextColor = Color(0xCCFFFFFF),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }

        // 🔥 Animated underline — SLIDES SMOOTHLY
        Box(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .height(3.dp)
                .width(55.dp)
                .offset(x = underlineOffset, y = 0.dp)
                .background(Color.White, RoundedCornerShape(20))
                .align(Alignment.BottomStart)
        )
    }
}


// ======================================================
// PREVIEW
// ======================================================
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController(),
        userName = "Dave Kwagala"
    )
}
