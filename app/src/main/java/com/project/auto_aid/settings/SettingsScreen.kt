package com.project.auto_aid.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.project.auto_aid.screens.AppBottomNavigationBar
import kotlinx.coroutines.tasks.await

// =======================================================
// SETTINGS SCREEN – ENTRY
// =======================================================
@Composable
fun SettingsScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        println("✅ SettingsScreen opened")
    }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()

    var firstName by remember { mutableStateOf<String?>(null) }
    var otherName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "Not Available") }

    LaunchedEffect(user?.uid) {
        try {
            val uid = user?.uid ?: return@LaunchedEffect
            val doc = firestore.collection("users").document(uid).get().await()
            firstName = doc.getString("firstName") ?: "User"
            otherName = doc.getString("otherName") ?: ""
        } catch (e: Exception) {
            firstName = "User"
            otherName = ""
        }
    }

    SettingsScreenUI(
        fullName = listOfNotNull(firstName, otherName).joinToString(" "),
        email = email,
        navController = navController,
        loading = firstName == null,
        onLogoutClick = {
            auth.signOut()
            navController.navigate(Routes.LoginScreen.route) {
                popUpTo(Routes.HomeScreen.route) { inclusive = true }
            }
        }
    )
}

// =======================================================
// SETTINGS UI
// =======================================================
@Composable
fun SettingsScreenUI(
    fullName: String,
    email: String,
    navController: NavHostController,
    loading: Boolean,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF2F6FA))
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // PROFILE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (loading) 0.6f else 1f)
                    .clickable(enabled = !loading) {
                        navController.navigate(Routes.UserInfoScreen.route)
                    },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(
                            painter = painterResource(id = R.drawable.logo01),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(4.5.dp, Color(0xFF0A9AD9), RoundedCornerShape(100.dp))
                        )

                        Spacer(modifier = Modifier.width(15.dp))

                        Column {
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(email, fontSize = 16.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(">", fontSize = 28.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // MENU
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(10.dp)) {
                    MenuItem("ID Verification", R.drawable.id) {
                        navController.navigate(Routes.IDVerificationScreen.route)
                    }
                    MenuItem("Communications", R.drawable.communication)
                    MenuItem("Payment Methods", R.drawable.payment)
                    MenuItem("Wallet Settings", R.drawable.wallet)
                    MenuItem("Contact Us", R.drawable.contact)
                    MenuItem("About Us", R.drawable.about)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // LOGOUT
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            )
            {
                Text("Sign Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// =======================================================
// MENU ITEM
// =======================================================
@Composable
fun MenuItem(title: String, icon: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painterResource(icon), null, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(20.dp))
        Text(title, fontSize = 18.sp, modifier = Modifier.weight(1f))
        if (title == "ID Verification") {
            Text("Verify", color = Color(0xFF0A9AD8), fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(">", fontSize = 22.sp, color = Color.Gray)
    }
    HorizontalDivider()
}

// =======================================================
// PREVIEW
// =======================================================
@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SettingsScreenUI(
        fullName = "Dave Kwagala",
        email = "dave@gmail.com",
        navController = rememberNavController(),
        loading = false,
        onLogoutClick = {}
    )
}
