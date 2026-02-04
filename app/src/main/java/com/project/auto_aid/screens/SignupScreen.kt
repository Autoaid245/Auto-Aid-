package com.project.auto_aid.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.navigation.Routes

/* ================= SIGNUP SCREEN ================= */

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var role by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    var businessType by remember { mutableStateOf("") }
    var subscription by remember { mutableStateOf("monthly") }

    val passwordStrength = remember(password) {
        when {
            password.length < 4 -> "Weak"
            password.any { it.isUpperCase() } &&
                    password.any { it.isDigit() } &&
                    password.any { "!@#\$%^&*".contains(it) } -> "Strong"
            else -> "Medium"
        }
    }

    /* ================= ROLE SELECTION ================= */

    if (role == null) {
        RoleSelection { role = it }
        return
    }

    /* ================= MAIN UI ================= */

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Create Account", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Fast help at your location", color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Input("Full Name", name) { name = it }
                Input("Email Address", email) { email = it }

                Input(
                    label = "Phone (776 123456)",
                    value = phone
                ) { phone = formatUgandaPhone(it) }

                PasswordInput(
                    label = "Password",
                    value = password,
                    show = showPassword,
                    toggle = { showPassword = !showPassword }
                ) { password = it }

                PasswordInput(
                    label = "Confirm Password",
                    value = confirmPassword,
                    show = showConfirm,
                    toggle = { showConfirm = !showConfirm }
                ) { confirmPassword = it }

                if (password.isNotEmpty()) {
                    Text(
                        "$passwordStrength Password",
                        fontWeight = FontWeight.Bold,
                        color = when (passwordStrength) {
                            "Strong" -> Color(0xFF2E7D32)
                            "Medium" -> Color(0xFFF9A825)
                            else -> Color.Red
                        }
                    )
                }

                if (role == "provider") {
                    Spacer(modifier = Modifier.height(10.dp))

                    Dropdown(
                        label = "Service Type",
                        options = listOf("garage", "fuel", "towing", "ambulance"),
                        selected = businessType
                    ) { businessType = it }

                    Dropdown(
                        label = "Subscription",
                        options = listOf("monthly", "quarterly", "yearly"),
                        selected = subscription
                    ) { subscription = it }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (loading) return@Button

                if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                    toast(context, "Fill in all fields")
                    return@Button
                }

                if (!isValidUgandaPhone(phone)) {
                    toast(context, "Invalid Uganda phone number")
                    return@Button
                }

                if (password != confirmPassword) {
                    toast(context, "Passwords do not match")
                    return@Button
                }

                loading = true

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->

                        val uid = result.user!!.uid

                        val data = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "phone" to "+256${phone.replace(" ", "")}",
                            "role" to role,
                            "businessType" to businessType,
                            "subscription" to subscription
                        )

                        db.collection("users").document(uid)
                            .set(data)
                            .addOnSuccessListener {
                                loading = false
                                navController.navigate(Routes.LoginScreen.route) {
                                    popUpTo(Routes.SignupScreen.route) { inclusive = true }
                                }
                            }
                            .addOnFailureListener {
                                loading = false
                                toast(context, it.message ?: "Failed to save user")
                            }
                    }
                    .addOnFailureListener {
                        loading = false
                        toast(context, it.message ?: "Signup failed")
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !loading,
            shape = RoundedCornerShape(28.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Continue", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate(Routes.LoginScreen.route)
        }) {
            Text("Already have an account? Login", color = Color(0xFF0A9AD9))
        }
    }
}

/* ================= COMPONENTS ================= */

@Composable
fun Input(label: String, value: String, onChange: (String) -> Unit) {
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordInput(
    label: String,
    value: String,
    show: Boolean,
    toggle: () -> Unit,
    onChange: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation =
            if (show) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Text(if (show) "🙈" else "👁️", modifier = Modifier.clickable { toggle() })
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Dropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(10.dp))

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

/* ================= ROLE SELECTION ================= */

@Composable
fun RoleSelection(onSelect: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sign Up As", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onSelect("user") }, modifier = Modifier.fillMaxWidth()) {
                    Text("👤 User")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { onSelect("provider") }, modifier = Modifier.fillMaxWidth()) {
                    Text("🛠 Service Provider")
                }
            }
        }
    }
}

/* ================= UTILS ================= */

fun formatUgandaPhone(input: String): String {
    val digits = input.filter { it.isDigit() }.take(9)
    return if (digits.length > 3)
        digits.substring(0, 3) + " " + digits.substring(3)
    else digits
}

fun isValidUgandaPhone(phone: String): Boolean {
    val cleaned = phone.replace(" ", "")
    val prefix = cleaned.take(2)
    return cleaned.length == 9 && prefix in listOf("70", "74", "75", "76", "77", "78")
}

fun toast(context: android.content.Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true)
@Composable
fun RoleSelectionPreview() {
    RoleSelection {}
}
