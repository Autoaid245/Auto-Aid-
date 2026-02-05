package com.project.auto_aid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

private val AppBlue = Color(0xFF0A9AD8)
private val SoftBg = Color(0xFFF6F8FB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Promotions") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(SoftBg)
                .padding(16.dp)
        ) {

            Text("Special Offers for You 🎉", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Unlock more features and enjoy better services.", color = Color.Gray)

            Spacer(Modifier.height(24.dp))

            PromotionCard(
                icon = Icons.Default.VerifiedUser,
                title = "Get Verified Faster",
                description = "Verify your identity and enjoy quicker service response.",
                buttonText = "Verify Now"
            ) {
                navController.navigate(Routes.UserInfoScreen.route)
            }
        }
    }
}

@Composable
private fun PromotionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Icon(icon, null, tint = AppBlue, modifier = Modifier.size(36.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Text(description, color = Color.Gray)

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppBlue)
            ) {
                Text(buttonText)
            }
        }
    }
}