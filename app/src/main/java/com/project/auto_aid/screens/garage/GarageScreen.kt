package com.project.auto_aid.screens.garage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@Composable
fun GarageScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF9F9F9))
    ) {

        // ================= BACK =================
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("← Back to Dashboard")
        }

        // ================= HERO =================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0895DD))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Garage Support",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Emergency breakdown & on-site repair services",
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⏱ Average response time: 15–30 minutes",
                color = Color.White,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate(Routes.GarageRequestScreen.route)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    "Request Emergency Assistance",
                    color = Color(0xFF0895DD),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= TRUST INDICATORS =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TrustItem("✔ Verified Mechanics")
            TrustItem("⭐ 4.8 Rating")
            TrustItem("📍 Nationwide")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ================= FEATURES =================
        SectionTitle("What We Offer")

        FeatureGrid(
            icon = Icons.Default.Build,
            title = "Mechanical Diagnosis",
            desc = "Instant assessment of breakdowns and engine issues."
        )

        FeatureGrid(
            icon = Icons.Default.BatteryChargingFull,
            title = "Battery Jump-start",
            desc = "Dead battery? We help you get moving instantly."
        )

        FeatureGrid(
            icon = Icons.Default.Settings,
            title = "Tyre Replacement",
            desc = "Flat tyre replacement or quick on-spot fixing."
        )

        FeatureGrid(
            icon = Icons.Default.CarCrash,
            title = "Minor Repairs",
            desc = "Small breakdowns fixed on-site by certified mechanics."
        )

        Spacer(modifier = Modifier.height(30.dp))

        // ================= HOW IT WORKS =================
        SectionTitle("How It Works")

        HowStep("01", "Tell Us Your Issue", "Describe what happened — engine, tyre, battery.")
        HowStep("02", "Nearest Garage Assigned", "We locate the closest available mechanic.")
        HowStep("03", "Mechanic Accepts Request", "A verified mechanic prepares to help.")
        HowStep("04", "Track Mechanic Live", "Monitor arrival in real time.")

        Spacer(modifier = Modifier.height(30.dp))

        // ================= CTA =================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Need Emergency Help Now?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Certified mechanics available 24/7 across Uganda.",
                    color = Color.White,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.GarageRequestScreen.route)
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Request Garage Assistance")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ======================================================
// REUSABLE COMPONENTS
// ======================================================

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
fun FeatureGrid(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE0F2FE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF0284C7))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(desc, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HowStep(
    step: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFF0895DD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(title, fontWeight = FontWeight.Bold)
            Text(desc, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun TrustItem(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF374151)
    )
}
