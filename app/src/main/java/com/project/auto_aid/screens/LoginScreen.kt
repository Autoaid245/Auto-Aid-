package com.project.auto_aid.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.auto_aid.R
import com.project.auto_aid.authentcation.presentation.components.SocialMediaOptions
import com.project.auto_aid.navigation.Routes
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import com.project.auto_aid.authentcation.presentation.components.GoogleAuthHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController?) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            GoogleAuthHelper.handleResult(
                data = result.data,
                onSuccess = {
                    Toast.makeText(context, "Google login successful", Toast.LENGTH_SHORT).show()
                    navController?.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.LoginScreen.route) { inclusive = true }
                    }
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Faint background image
        Image(
            painter = painterResource(id = R.drawable.logo14),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop,
            alpha = 1.18f   // Must be between 0f..1f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.logo01),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(10.1.dp, Color(0xFF0A9AD9), RoundedCornerShape(100.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Welcome back!  Thank You", fontSize = 38.sp, fontWeight = FontWeight.Bold)
            Text("Login to your Account", color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            // ================= CARD WRAPPING INPUTS =================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // EMAIL
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    // PASSWORD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Image(
                                    painter = painterResource(
                                        id = if (showPassword) R.drawable.no_see else R.drawable.see
                                    ),
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Forgot password?",
                        color = Color(0xFF0A9AD9),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                navController?.navigate(Routes.ForgotPasswordScreen.route)
                            }
                    )



                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LOGIN BUTTON
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        Firebase.auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                    navController?.navigate(Routes.HomeScreen.route) {
                                        popUpTo(Routes.LoginScreen.route) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        task.exception?.message ?: "Login failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Enter email & password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A9AD9),
                    contentColor = Color.White
                )
            ) {
                Text("Login", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Or sign in with", color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))


            SocialMediaOptions(
                onGoogleClick = {
                    val intent = GoogleAuthHelper.getSignInIntent(context)
                    googleLauncher.launch(intent)
                },
                onFacebookClick = {
                    Toast.makeText(context, "Facebook login coming soon", Toast.LENGTH_SHORT).show()
                },
                onTikTokClick = {
                    Toast.makeText(context, "TikTok login coming soon", Toast.LENGTH_SHORT).show()
                },
                onInstagramClick = {
                    Toast.makeText(context, "Instagram login coming soon", Toast.LENGTH_SHORT).show()
                }
            )


            Spacer(modifier = Modifier.height(10.dp))

            // SIGNUP REDIRECT
            TextButton(
                onClick = {
                    navController?.navigate(Routes.SignupScreen.route)
                }
            ) {
                Text(
                    buildAnnotatedString {
                        append("Don't have an Account? ")
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF0A9AD9),
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        ) { append("Sign Up") }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
