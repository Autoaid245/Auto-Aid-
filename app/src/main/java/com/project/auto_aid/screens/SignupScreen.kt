package com.project.auto_aid.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.project.auto_aid.authentcation.presentation.components.GoogleAuthHelper
import com.project.auto_aid.authentcation.presentation.components.SocialMediaOptions
import com.project.auto_aid.navigation.Routes


@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val auth = if (!isPreview) FirebaseAuth.getInstance() else null
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null
    var firstName by remember { mutableStateOf("") }
    var otherName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            GoogleAuthHelper.handleResult(
                data = result.data,
                onSuccess = {
                    Toast.makeText(context, "Google login successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.LoginScreen.route) { inclusive = true }
                    }
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            )
        }
    }


    // ✅ ROLE STATE (NEW)
    var selectedRole by remember { mutableStateOf("USER") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            // ===================== TOP IMAGE =====================
            Image(
                painter = painterResource(id = R.drawable.logo14),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Sign Up As",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // ===================== ROLE SELECTION (ADDED ONLY) =====================
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoleOption("User", selectedRole == "USER") {
                    selectedRole = "USER"
                }
                RoleOption("Provider", selectedRole == "SERVICE_PROVIDER") {
                    selectedRole = "SERVICE_PROVIDER"
                }

            }

            Spacer(modifier = Modifier.height(0.dp))

            // ===================== FORM CARD =====================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    OutlinedTextField(
                        value = otherName,
                        onValueChange = { otherName = it },
                        label = { Text("Other Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // ✅ PHONE NUMBER (UGANDA)
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
                        ),
                        leadingIcon = {
                            Text(
                                text = "+256",
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        },
                        placeholder = { Text("") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation =
                            if (showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Image(
                                    painter =
                                        if (showPassword)
                                            painterResource(id = R.drawable.no_see)
                                        else
                                            painterResource(id = R.drawable.see),
                                    contentDescription = "Toggle password"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


            // ===================== SIGN UP BUTTON =====================
            Button(
                onClick = {
                    if (
                        firstName.isBlank() ||
                        otherName.isBlank() ||
                        phone.isBlank() ||
                        email.isBlank() ||
                        password.isBlank()
                    ) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    auth?.createUserWithEmailAndPassword(email, password)
                        ?.addOnSuccessListener { result ->
                            val userId = result.user?.uid ?: return@addOnSuccessListener

                            val userData = hashMapOf(
                                "firstName" to firstName,
                                "otherName" to otherName,
                                "phone" to phone,
                                "email" to email,
                                "uid" to userId,
                                "role" to selectedRole // ✅ SAVED
                            )

                            db?.collection("users")?.document(userId)
                                ?.set(userData)
                                ?.addOnSuccessListener {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Signed up as $selectedRole",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate(Routes.HomeScreen.route) {
                                        popUpTo(Routes.SignupScreen.route) { inclusive = true }
                                    }
                                }
                                ?.addOnFailureListener {
                                    isLoading = false
                                    Toast.makeText(context, "Error saving data", Toast.LENGTH_LONG).show()
                                }
                        }
                        ?.addOnFailureListener {
                            isLoading = false
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A9AD9),
                    contentColor = Color.White
                )
            ) {
                Text(if (isLoading) "Signing up..." else "Sign Up")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===================== LOGIN TEXT =====================
            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF0A9AD9),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    ) { append("Login") }
                },
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LoginScreen.route)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            // ===================== SOCIAL MEDIA (UNCHANGED) =====================
            SocialMediaOptions(
                onGoogleClick = {
                    val intent = GoogleAuthHelper.getSignInIntent(context)
                    val googleLauncher = null
                    googleLauncher.launch()
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

        }
    }
}

private fun Nothing?.launch() {}

// ===================== ROLE OPTION CARD =====================
@Composable
fun RoleOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color(0xFF0A9AD9) else Color.Black
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Bottom indicator line
        Box(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth(0.6f)
                .background(
                    if (selected) Color(0xFF0A9AD9) else Color.Transparent,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}
