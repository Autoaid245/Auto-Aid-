package com.project.auto_aid.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // ================= STATES =================
    var role by remember { mutableStateOf<String?>(null) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    var passwordStrength by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    fun evaluateStrength(value: String): String =
        when {
            value.length < 4 -> "Weak"
            value.length >= 8 &&
                    value.any { it.isUpperCase() } &&
                    value.any { it.isDigit() } &&
                    value.any { "!@#\$%^&*".contains(it) } -> "Strong"
            else -> "Medium"
        }

    // ================= ROLE SELECTION =================
    if (role == null) {
        RoleSelection { role = it }
        return
    }

    // ================= RESPONSIVE LAYOUT =================
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isMobile = maxWidth < 600.dp

        if (isMobile) {
            // ================= MOBILE =================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo14),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Welcome", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Fast help at your location.", color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                SignupForm(
                    role = role!!,
                    name = name,
                    email = email,
                    phone = phone,
                    password = password,
                    confirmPassword = confirmPassword,
                    showPassword = showPassword,
                    showConfirm = showConfirm,
                    passwordStrength = passwordStrength,
                    businessType = businessType,
                    loading = loading,
                    onNameChange = { name = it },
                    onEmailChange = { email = it },
                    onPhoneChange = { phone = it },
                    onPasswordChange = {
                        password = it
                        passwordStrength = evaluateStrength(it)
                    },
                    onConfirmChange = { confirmPassword = it },
                    togglePassword = { showPassword = !showPassword },
                    toggleConfirm = { showConfirm = !showConfirm },
                    onBusinessTypeChange = { businessType = it },
                    onSubmit = {
                        signup(
                            context, auth, db,
                            name, email, phone, password,
                            confirmPassword, role!!, businessType,
                            navController
                        ) { loading = it }
                    }
                )
            }

        } else {
            // ================= TABLET / DESKTOP =================
            Row(modifier = Modifier.fillMaxSize()) {

                // LEFT PANEL
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF0A9AD9)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo14),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Welcome", color = Color.White, fontSize = 28.sp)
                    Text(
                        "Fast help at your location.",
                        color = Color.White.copy(0.8f)
                    )
                }

                // RIGHT PANEL
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.padding(24.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        SignupForm(
                            role = role!!,
                            name = name,
                            email = email,
                            phone = phone,
                            password = password,
                            confirmPassword = confirmPassword,
                            showPassword = showPassword,
                            showConfirm = showConfirm,
                            passwordStrength = passwordStrength,
                            businessType = businessType,
                            loading = loading,
                            onNameChange = { name = it },
                            onEmailChange = { email = it },
                            onPhoneChange = { phone = it },
                            onPasswordChange = {
                                password = it
                                passwordStrength = evaluateStrength(it)
                            },
                            onConfirmChange = { confirmPassword = it },
                            togglePassword = { showPassword = !showPassword },
                            toggleConfirm = { showConfirm = !showConfirm },
                            onBusinessTypeChange = { businessType = it },
                            onSubmit = {
                                signup(
                                    context, auth, db,
                                    name, email, phone, password,
                                    confirmPassword, role!!, businessType,
                                    navController
                                ) { loading = it }
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ================= FORM ================= */

@Composable
fun SignupForm(
    role: String,
    name: String,
    email: String,
    phone: String,
    password: String,
    confirmPassword: String,
    showPassword: Boolean,
    showConfirm: Boolean,
    passwordStrength: String,
    businessType: String,
    loading: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    togglePassword: () -> Unit,
    toggleConfirm: () -> Unit,
    onBusinessTypeChange: (String) -> Unit,
    onSubmit: () -> Unit
) {

    Column(modifier = Modifier.padding(20.dp)) {

        Text("AutoAID", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Emergency Services", color = Color.Gray)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(name, onNameChange, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(email, onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(phone, onPhoneChange, label = { Text("Phone") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    if (showPassword) "🙈" else "👁️",
                    modifier = Modifier.clickable { togglePassword() }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmChange,
            label = { Text("Confirm Password") },
            visualTransformation =
                if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    if (showConfirm) "🙈" else "👁️",
                    modifier = Modifier.clickable { toggleConfirm() }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (password.isNotEmpty()) {
            Text(
                "$passwordStrength Password",
                color = when (passwordStrength) {
                    "Weak" -> Color.Red
                    "Medium" -> Color(0xFFFF9800)
                    else -> Color.Green
                }
            )
        }

        if (role == "PROVIDER") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = businessType,
                onValueChange = onBusinessTypeChange,
                label = { Text("Service Type") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            Text(if (loading) "Processing..." else "Continue")
        }
    }
}

/* ================= ROLE SELECTION ================= */

@Composable
fun RoleSelection(onSelect: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("AutoAID", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Emergency Services", color = Color.Gray)

                Spacer(modifier = Modifier.height(20.dp))

                Text("Sign Up As", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onSelect("USER") }, modifier = Modifier.fillMaxWidth()) {
                    Text("👤 User")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = { onSelect("PROVIDER") }, modifier = Modifier.fillMaxWidth()) {
                    Text("🛠 Service Provider")
                }
            }
        }
    }
}

/* ================= SIGNUP LOGIC ================= */

fun signup(
    context: android.content.Context,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    name: String,
    email: String,
    phone: String,
    password: String,
    confirmPassword: String,
    role: String,
    businessType: String,
    navController: NavController,
    setLoading: (Boolean) -> Unit
) {
    if (name.isBlank() || email.isBlank() || phone.isBlank() || password != confirmPassword) {
        Toast.makeText(context, "Check your details", Toast.LENGTH_SHORT).show()
        return
    }

    setLoading(true)

    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            val uid = it.user!!.uid
            val data = hashMapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "role" to role,
                "businessType" to businessType
            )

            db.collection("users").document(uid).set(data)
                .addOnSuccessListener {
                    setLoading(false)
                    navController.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.SignupScreen.route) { inclusive = true }
                    }
                }
        }
        .addOnFailureListener {
            setLoading(false)
            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
}
