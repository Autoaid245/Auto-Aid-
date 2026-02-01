package com.project.auto_aid.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R

@Composable
fun UserInfoScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    var fullName by remember { mutableStateOf("Loading...") }
    var phone by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf(user?.email ?: "Not Available") }
    var rating by remember { mutableStateOf("4.8") }
    var userId by remember { mutableStateOf(user?.uid ?: "Unknown") }

    // Fetch user data
    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    val fName = doc.getString("firstName") ?: ""
                    val oName = doc.getString("otherName") ?: ""
                    fullName = "$fName $oName"
                    phone = doc.getString("phone") ?: "Unknown"
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F6FA))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // ========= TITLE ==========
        Text(
            text = "Personal Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A9AD8)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // ========= PROFILE IMAGE ==========
        Image(
            painter = painterResource(id = R.drawable.logo1), // Your profile picture
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(25.dp))

        // ========= MAIN CARD ==========
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                InfoRow("Full Name", fullName)
                Divider(color = Color(0xFFE0E0E0))

                InfoRow("Phone Number", phone)
                Divider(color = Color(0xFFE0E0E0))

                InfoRow("Email Address", email)
                Divider(color = Color(0xFFE0E0E0))

                InfoRow("User ID", userId)
                Divider(color = Color(0xFFE0E0E0))

                // ========= MY RATING (left aligned) ==========
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = "My Rating",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = "★",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107) // GOLD STAR
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = rating,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D2D2D)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ========= BACK BUTTON ==========
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD8)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "Back",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// =======================================================
// INFO ROW — LEFT ALIGNED VERSION
// =======================================================
@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 14.dp)) {

        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D2D2D),
            maxLines = 1
        )
    }
}
