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

@Composable
fun LoginScreen(navController: NavController) {


    val scrollState = rememberScrollState()
    val isPreview = LocalInspectionMode.current

    val auth = if (isPreview) null else FirebaseAuth.getInstance()
    val db = if (isPreview) null else FirebaseFirestore.getInstance()

    var role by remember { mutableStateOf(if (isPreview) "User" else null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    if (role == null) {
        RoleSelectionScreen { role = it }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        HeroImageSlider()

        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .offset(y = (-35).dp)
                    .size(110.dp)
                    .border(6.dp, Color(0xFF0A9AD9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo01),
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(130.dp).clip(CircleShape).background(Color.White)
                )
            }

            Text("Welcome back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Login to your account", color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            if (errorMsg.isNotEmpty()) Text(errorMsg, color = Color.Red)

            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(0.9f))
            Spacer(Modifier.height(6.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = { Text(if (showPassword) "🙈" else "👁️", Modifier.clickable { showPassword = !showPassword }) },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Forgot password?",
                color = Color(0xFF0A9AD9),
                modifier = Modifier.align(Alignment.End).clickable {
                    if (!isPreview) navController.navigate(Routes.ForgotPasswordScreen.route)
                }
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (loading || isPreview) return@Button
                    if (email.isBlank() || password.isBlank()) { errorMsg = "Email and password cannot be empty"; return@Button }

                    loading = true
                    errorMsg = ""

                    auth?.signInWithEmailAndPassword(email.trim(), password)
                        ?.addOnSuccessListener { result ->
                            val uid = result.user?.uid ?: return@addOnSuccessListener
                            db?.collection("users")?.document(uid)?.get()
                                ?.addOnSuccessListener { doc ->
                                    loading = false
                                    val savedRole = doc.getString("role")

                                    if (savedRole?.lowercase() != role!!.lowercase()) {
                                        errorMsg = "Invalid role selected"
                                        auth.signOut()
                                        return@addOnSuccessListener
                                    }

                                    when (role!!.lowercase()) {
                                        "user" -> navController.navigate(Routes.HomeScreen.route) { popUpTo(0) }
                                        "provider" -> navController.navigate(Routes.ProviderDashboard.route) { popUpTo(0) }
                                    }
                                }
                                ?.addOnFailureListener { loading = false; errorMsg = "Failed to fetch user data" }
                        }
                        ?.addOnFailureListener { loading = false; errorMsg = it.localizedMessage ?: "Login failed" }
                },
                modifier = Modifier.fillMaxWidth(0.9f).height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                if (loading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                else Text("Login")
            }

            Spacer(Modifier.height(12.dp))
            SocialMediaOptions({}, {}, {}, {})

            TextButton(onClick = { if (!isPreview) navController.navigate(Routes.SignupScreen.route) }) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) { append("Don’t have an account? ") }
                    withStyle(SpanStyle(color = Color(0xFF0A9AD9), fontWeight = FontWeight.Bold)) { append("Sign Up") }
                })
            }
        }
    }


}

/* ROLE SELECTION SCREEN */
@Composable
fun RoleSelectionScreen(onSelect: (String) -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


        Image(painterResource(R.drawable.fuel), null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)))

        Card(shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Text("Login As", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))

                Button(onClick = { onSelect("User") }, modifier = Modifier.fillMaxWidth()) { Text("User") }
                Spacer(Modifier.height(14.dp))
                Button(onClick = { onSelect("provider") }, modifier = Modifier.fillMaxWidth()) { Text("Service Provider") }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
