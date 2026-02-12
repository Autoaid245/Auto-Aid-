package com.project.auto_aid.screens.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SearchingMechanicScreen(navController: NavHostController) {

    // 🔁 AUTO REDIRECT (disabled in preview-safe way)
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Routes.MechanicAssignedScreen.route) {
            popUpTo(Routes.GarageRequestScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Searching for a mechanic…",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please wait while we find the nearest available mechanic.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchingMechanicScreenPreview() {
    val navController = rememberNavController()
    SearchingMechanicScreen(navController = navController)
}
