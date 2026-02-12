package com.project.auto_aid.screens

import Components.HeroImageSlider
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.draw.blur
=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.authentcation.presentation.components.SocialMediaOptions
import com.project.auto_aid.navigation.Routes

/* =====================================================
   LOGIN SCREEN
===================================================== */
@Composable
fun LoginScreen(navController: NavController) {

    val scrollState = rememberScrollState()
    val isPreview = LocalInspectionMode.current

    val auth = if (isPreview) null else FirebaseAuth.getInstance()
    val db = if (isPreview) null else FirebaseFirestore.getInstance()

<<<<<<< HEAD
    // ✅ PREVIEW-SAFE ROLE (FIX #1)
=======
    // ✅ PREVIEW-SAFE ROLE (UNCHANGED)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    var role by remember {
        mutableStateOf(if (isPreview) "User" else null)
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

<<<<<<< HEAD
    // 🔹 ROLE SELECTION FIRST (UNCHANGED LOGIC)
=======
    // 🔹 ROLE SELECTION FIRST (UNCHANGED)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    if (role == null) {
        RoleSelectionScreen { selectedRole ->
            role = selectedRole
        }
        return
    }

<<<<<<< HEAD
    Column(modifier = Modifier.fillMaxSize()) {
=======
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

        HeroImageSlider()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Box(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .size(110.dp)
                    .border(
                        width = 6.dp,
                        color = Color(0xFF0A9AD9),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo01),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Welcome back!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text("Login to your account", color = Color.Gray)

                Spacer(modifier = Modifier.height(3.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .padding(1.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (errorMsg.isNotEmpty()) {
                            Text(errorMsg, color = Color.Red)
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation =
                                if (showPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                            trailingIcon = {
                                Text(
                                    text = if (showPassword) "🙈" else "👁️",
                                    modifier = Modifier.clickable {
                                        showPassword = !showPassword
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Forgot password?",
                            color = Color(0xFF0A9AD9),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    if (!isPreview) {
                                        navController.navigate(Routes.ForgotPasswordScreen.route)
                                    }
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (loading || isPreview) return@Button
<<<<<<< HEAD
=======

                        if (email.isBlank() || password.isBlank()) {
                            errorMsg = "Email and password cannot be empty"
                            return@Button
                        }

                        loading = true
                        errorMsg = ""

                        auth?.signInWithEmailAndPassword(email.trim(), password)
                            ?.addOnSuccessListener { result ->

                                val uid = result.user?.uid ?: return@addOnSuccessListener

                                db?.collection("users")
                                    ?.document(uid)
                                    ?.get()
                                    ?.addOnSuccessListener { doc ->

                                        loading = false

                                        val savedRole = doc.getString("role")

                                        // ✅ FIX #1: CASE + NULL SAFE ROLE CHECK
                                        if (
                                            savedRole == null ||
                                            savedRole.lowercase() != role!!.lowercase()
                                        ) {
                                            errorMsg = "Invalid role selected"
                                            auth.signOut()
                                            return@addOnSuccessListener
                                        }

                                        // ✅ FIX #2: CASE-SAFE NAVIGATION
                                        when (role!!.lowercase()) {
                                            "user" -> navController.navigate(Routes.HomeScreen.route) {
                                                popUpTo(0)
                                            }
                                            "provider" -> navController.navigate(Routes.ProviderDashboard.route) {
                                                popUpTo(0)
                                            }
                                        }
                                    }
                                    ?.addOnFailureListener {
                                        loading = false
                                        errorMsg = "Failed to fetch user data"
                                    }
                            }
                            ?.addOnFailureListener {
                                loading = false
                                errorMsg = it.localizedMessage ?: "Login failed"
                            }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    enabled = !loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                ) {
<<<<<<< HEAD
                    Text("Login", fontSize = 18.sp)
=======
                    if (loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Login", fontSize = 18.sp)
                    }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                }

                Spacer(modifier = Modifier.height(15.dp))

                Text("Or Sign Up with", color = Color.Gray)

                Spacer(modifier = Modifier.height(5.dp))

                SocialMediaOptions(
                    onGoogleClick = {},
                    onFacebookClick = {},
                    onTikTokClick = {},
                    onInstagramClick = {}
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        if (!isPreview) {
                            navController.navigate(Routes.SignupScreen.route)
                        }
                    }
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color.Gray)) {
                                append("Don’t have an account? ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF0A9AD9),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            ) {
                                append("Sign Up")
                            }
                        }
                    )
                }
            }
        }
    }
}

/* =====================================================
<<<<<<< HEAD
   ROLE SELECTION SCREEN
=======
   ROLE SELECTION SCREEN (UNCHANGED)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
===================================================== */
@Composable
fun RoleSelectionScreen(onSelect: (String) -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
<<<<<<< HEAD
            painter = painterResource(id = R.drawable.fuel), // 👈 choose your image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.55f))// 👈 BLUR STRENGTH
=======
            painter = painterResource(id = R.drawable.fuel),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
<<<<<<< HEAD
                .background(Color.Black.copy(alpha = 0.55f)) // ✅ contrast control
=======
                .background(Color.Black.copy(alpha = 0.55f))
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        )

        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Login As",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSelect("User") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                ) {
                    Text("User")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onSelect("provider") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                ) {
                    Text("Service Provider")
                }
            }
        }
    }
}

/* =====================================================
   PREVIEW
===================================================== */
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}