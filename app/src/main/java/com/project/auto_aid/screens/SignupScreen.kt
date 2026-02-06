package com.project.auto_aid.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi

/* ================= SIGNUP SCREEN ================= */

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current

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
                    password.any { "!@#$%^&*".contains(it) } -> "Strong"
            else -> "Medium"
        }
    }

    if (role == null) {
        RoleSelection { role = it }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeroImageSlider()

        Spacer(modifier = Modifier.height(6.dp))

        Text("Create Account", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Fast help at your location", color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)

        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Input("Full Name", name) { name = it }
                Input("Email Address", email) { email = it }

                UgandaPhoneInput(
                    phone = phone,
                    onPhoneChange = { phone = it },
                    isError = phone.isNotEmpty() && !isValidUgandaPhone(phone)
                )

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

                // ✅ NOW THESE SHOW CORRECTLY
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

        Spacer(modifier = Modifier.height(10.dp))

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

                toast(context, "Signup logic goes here")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD9),
                contentColor = Color.White
            )
        ) {
            Text("Continue", fontSize = 18.sp)
        }

        TextButton(onClick = {
            navController.navigate(Routes.LoginScreen.route)
        }) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) {
                        append("Already having an account? ")
                    }
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF0A9AD9),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    ) {
                        append("Login")
                    }
                }
            )
        }
    }
}

/* ================= HERO IMAGE SLIDER ================= */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageSlider() {

    val images = listOf(
        R.drawable.total_1,
        R.drawable.shell_2,
        R.drawable.logo14
    )

    val pagerState = rememberPagerState { images.size }

    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            pagerState.animateScrollToPage(
                (pagerState.currentPage + 1) % images.size
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) { page ->
        AsyncImage(
            model = images[page],
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/* ================= COMPONENTS ================= */

@Composable
fun Input(label: String, value: String, onChange: (String) -> Unit) {
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun UgandaPhoneInput(
    phone: String,
    onPhoneChange: (String) -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = phone,
        onValueChange = {
            onPhoneChange(it.filter { c -> c.isDigit() }.take(9))
        },
        label = { Text("Phone Number") },
        leadingIcon = { Text("+256 ") },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        supportingText = {
            if (isError) {
                Text("Enter a valid Ugandan number", color = MaterialTheme.colorScheme.error)
            }
        }
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
    Spacer(modifier = Modifier.height(5.dp))
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
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sign Up As", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSelect("User") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                )
                {
                    Text("User")
                }

                Spacer(modifier = Modifier.height(14.dp))


                Button(
                    onClick = { onSelect("Provider") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
                )
                {
                    Text("Service Provider")
                }


            }
        }
    }
}

/* ================= UTILS ================= */

fun isValidUgandaPhone(phone: String): Boolean {
    val prefixes = listOf("70", "74", "75", "76", "77", "78")
    return phone.length == 9 && prefixes.any { phone.startsWith(it) }
}

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}