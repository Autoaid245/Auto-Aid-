package com.project.auto_aid.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.delay

/* ================= LOGIN SCREEN ================= */

@Composable
fun LoginScreen(navController: NavController) {

    val isPreview = LocalInspectionMode.current

    val auth = if (isPreview) null else FirebaseAuth.getInstance()
    val db = if (isPreview) null else FirebaseFirestore.getInstance()


    var role by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    if (role == null) {
        RoleSelectionScreen { role = it }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {

        HeroImageSlider(previewMode = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(125.dp))

            Box(
                modifier = Modifier
                    .size(110.dp)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo01),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(440.dp)

                        .clip(RoundedCornerShape(20.dp))
                        .border(7.5.dp, Color(0xFF0A9AD9),
                            RoundedCornerShape(100.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Welcome back!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Login to your Account", color = Color.Gray)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(13.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    if (errorMsg.isNotEmpty()) {
                        Text(errorMsg, color = Color.Red)

                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation =
                            if (showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        trailingIcon = {
                            Text(
                                if (showPassword) "🙈" else "👁️",
                                modifier = Modifier.clickable {
                                    showPassword = !showPassword
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Forgot password?",
                        color = Color(0xFF0A9AD9),
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                navController.navigate(Routes.ForgotPasswordScreen.route)
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (loading) return@Button
                    if (email.isBlank() || password.isBlank()) {
                        errorMsg = "Enter email and password"
                        return@Button
                    }

                    loading = true
                    errorMsg = ""

                    auth?.signInWithEmailAndPassword(email, password)
                        ?.addOnSuccessListener { result ->
                            val uid = result.user!!.uid

                            db?.collection("users")?.document(uid)?.get()
                                ?.addOnSuccessListener { doc ->
                                    loading = false
                                    val userRole = doc.getString("role")?.lowercase()

                                    when (userRole) {
                                        role -> navController.navigate(Routes.HomeScreen.route)
                                        else -> {
                                            errorMsg = "Wrong role selected"
                                            auth.signOut()
                                        }
                                    }
                                }
                        }
                        ?.addOnFailureListener {
                            loading = false
                            errorMsg = it.message ?: "Login failed"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A9AD9),
                    contentColor = Color.White
                ),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Login", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Or sign in with", color = Color.Gray)

            Spacer(modifier = Modifier.height(13.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                AsyncImage(R.drawable.gmail, null, Modifier.size(42.dp))
                AsyncImage(R.drawable.fb, null, Modifier.size(42.dp))
                AsyncImage(R.drawable.ticktok, null, Modifier.size(42.dp))
                AsyncImage(R.drawable.instagram, null, Modifier.size(42.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = {
                navController.navigate(Routes.SignupScreen.route)
            })
            {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = Color.Gray)
                        ) {
                            append("Don’t have an account? ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF0A9AD9),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
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

/* ================= HERO IMAGE SLIDER ================= */


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageSlider(previewMode: Boolean) {

    val images = listOf(
        R.drawable.total_1,
        R.drawable.shell_2,
        R.drawable.logo14
    )

    val pagerState = rememberPagerState { images.size }

    if (!previewMode) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(4000)
                pagerState.animateScrollToPage(
                    (pagerState.currentPage + 1) % images.size
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)

    ) { page ->
        AsyncImage(
            model = images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

/* ================= ROLE SELECTION ================= */

@Composable
fun RoleSelectionScreen(onSelect: (String) -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.fuel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .blur(15.dp),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.1f))
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSelect("user") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                ) {
                    Text("User")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onSelect("provider") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
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

/* ================= PREVIEW ================= */
@Preview(showBackground = true, device = "spec:width=360dp,height=640dp")
@Composable
fun LoginScreenPreview() {

    LoginScreen(navController = rememberNavController())
}


