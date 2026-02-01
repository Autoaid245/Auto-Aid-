package com.project.auto_aid.selectDocument

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

@Composable
fun SelectDocumentScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // ---------------- BACK BUTTON ----------------
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arro2),
                contentDescription = "Back",
                tint = Color(0xFF03C5F5),  // 🔥 visible dark color
                modifier = Modifier.size(32.dp)  // 🔥 slightly bigger
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ---------------- TITLE ----------------
        Text(
            text = "Verify Identity",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A3A48)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // ---------------- COUNTRY TITLE ----------------
        Text(
            text = "What country is your document from?",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0A3A48)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // ---------------- COUNTRY BOX ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE1E4E8), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.flag),
                    contentDescription = "Uganda Flag",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "Uganda",
                    fontSize = 18.sp,
                    color = Color(0xFF0A3A48),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(35.dp))

        // ---------------- DOCUMENT TITLE ----------------
        Text(
            text = "Select a document",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A3A48)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // ---------------- NATIONAL ID ----------------
        DocumentOption(
            icon = R.drawable.id2,
            title = "National ID",
            onClick = {
                navController.navigate(Routes.IDVerificationScreen.route)
            }
        )

        // ---------------- PASSPORT ----------------
        DocumentOption(
            icon = R.drawable.pass,
            title = "Passport",
            onClick = {
                // TODO: Add passport verification later
            }
        )
    }
}

@Composable
fun DocumentOption(icon: Int, title: String, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0A3A48)
                )
            }

            Text(
                text = ">",
                fontSize = 26.sp,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}
