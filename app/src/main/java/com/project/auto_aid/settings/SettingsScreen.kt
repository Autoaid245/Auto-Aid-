package com.project.auto_aid.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.screens.AppBottomNavigationBar

// =======================================================
// SETTINGS SCREEN – Loads Firebase User Data
// =======================================================
@Composable
fun SettingsScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()

    var firstName by remember { mutableStateOf("Loading") }
    var otherName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "Not Available") }

    // LOAD NAME FROM FIRESTORE
    LaunchedEffect(user) {
        user?.uid?.let { uid ->
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    firstName = doc.getString("firstName") ?: "User"
                    otherName = doc.getString("otherName") ?: ""
                }
        }
    }

    SettingsScreenUI(
        fullName = "$firstName $otherName",
        email = email,
        navController = navController,
        onLogoutClick = {
            auth.signOut()
            navController.navigate(Routes.LoginScreen.route) { popUpTo(0) }
        }
    )
}

// =======================================================
// SETTINGS UI — FULL WORKING VERSION
// =======================================================
@Composable
fun SettingsScreenUI(
    fullName: String,
    email: String,
    navController: NavController? = null,
    onLogoutClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F6FA))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // ---------------- PROFILE CARD ----------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController?.navigate(Routes.UserInfoScreen.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painter = painterResource(R.drawable.logo1),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(75.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Column {
                            Text(fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text(email, fontSize = 16.sp, color = Color.Gray)
                        }
                    }

                    Text(">", fontSize = 28.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // ---------------- MENU CARD ----------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Column(modifier = Modifier.padding(10.dp)) {

                    MenuItem("ID Verification", R.drawable.id) {
                        navController?.navigate(Routes.IDVerificationScreen.route)
                    }

                    MenuItem("Communications", R.drawable.communication)

                    MenuItem("Payment Methods", R.drawable.payment)

                    MenuItem("Wallet Settings", R.drawable.wallet)

                    MenuItem("Contact Us", R.drawable.contact)

                    MenuItem("About", R.drawable.about)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // ---------------- LOGOUT BUTTON ----------------
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A9AD8),
                    contentColor = Color.White
                )
            ) {
                Text("Logout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        // ---------------- BOTTOM NAV BAR ----------------
        navController?.let { controller ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                AppBottomNavigationBar(navController = controller as NavHostController)
            }
        }
    }
}

// =======================================================
// MENU ITEM WITH "VERIFY" BADGE FOR ID VERIFICATION
// =======================================================
@Composable
fun MenuItem(
    title: String,
    icon: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )

        // Only for ID Verification
        if (title == "ID Verification") {
            Text(
                text = "Verify",
                fontSize = 16.sp,
                color = Color(0xFF0A9AD8),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Text(">", fontSize = 22.sp, color = Color.Gray)
    }

    HorizontalDivider(color = Color(0xFFE0E0E0))
}

// =======================================================
// PREVIEW ONLY
// =======================================================
@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreenUI(
        fullName = "Dave Kwagala",
        email = "dave@gmail.com"
    )
}
