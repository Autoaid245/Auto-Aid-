package com.project.auto_aid.screens.towing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun TowingScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(20.dp)
    ) {

        /* ===== HEADER ===== */
        Text(
            text = "Towing Service",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Fast, reliable towing help when you need it.",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        /* ===== REQUEST TOW ===== */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.TowingRequestScreen.route)
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Build, // ✅ ALWAYS EXISTS
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Request Tow", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Get immediate towing assistance",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ===== ACTIVE REQUEST ===== */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.TowingActiveScreen.route)
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Active Request", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Track your ongoing tow",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ===== HISTORY ===== */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Routes.TowingHistoryScreen.route)
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = Color(0xFF0284C7),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Towing History", fontWeight = FontWeight.SemiBold)
                    Text(
                        "View past towing requests",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        /* ===== BACK ===== */
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}