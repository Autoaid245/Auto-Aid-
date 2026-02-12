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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
<<<<<<< HEAD
import androidx.compose.ui.tooling.preview.Preview
=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
<<<<<<< HEAD
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.blur
=======
import coil.compose.AsyncImage
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

/* ================= SIGNUP SCREEN ================= */

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

<<<<<<< HEAD
    var role by remember { mutableStateOf("") } // change if needed
=======
    var role by remember { mutableStateOf("") }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var businessType by remember { mutableStateOf("") }
    var subscription by remember { mutableStateOf("") }
<<<<<<< HEAD

    if (role.isEmpty()) {
        RoleSelection { selectedRole ->
            role = selectedRole
=======
    var loading by remember { mutableStateOf(false) }

    if (role.isEmpty()) {
        RoleSelection { selectedRole ->
            role = selectedRole.lowercase()
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        HeroImageSlider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

<<<<<<< HEAD
        Spacer(modifier = Modifier.height(8.dp))
=======
        Spacer(modifier = Modifier.height(4.dp))

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        Text(
            "Fast help at your location",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

<<<<<<< HEAD
        Spacer(modifier = Modifier.height(5.dp))
=======
        Spacer(modifier = Modifier.height(6.dp))
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {

                Input("Full Name", name) { name = it }
                Input("Email Address", email) { email = it }

                UgandaPhoneInput(
                    phone = phone,
                    onPhoneChange = { phone = it },
                    isError = phone.isNotEmpty() && !isValidUgandaPhone(phone)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        PasswordInput(
                            label = "Password",
                            value = password,
                            show = showPassword,
                            toggle = { showPassword = !showPassword }
                        ) { password = it }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        PasswordInput(
                            label = "Confirm",
                            value = confirmPassword,
                            show = showConfirm,
                            toggle = { showConfirm = !showConfirm }
                        ) { confirmPassword = it }
                    }
                }

<<<<<<< HEAD
                if (role.equals("provider", ignoreCase = true)) {
=======
                if (role == "provider") {
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            Dropdown(
                                label = "Service Type",
                                options = listOf("Garage", "Fuel", "Towing", "Ambulance"),
                                selected = businessType
                            ) { businessType = it }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            Dropdown(
                                label = "Subscription",
                                options = listOf("Monthly", "Quarterly", "Yearly"),
                                selected = subscription
                            ) { subscription = it }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
<<<<<<< HEAD
            onClick = {
=======
            enabled = !loading,
            onClick = {

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
                    toast(context, "Fill in all fields")
                    return@Button
                }
<<<<<<< HEAD
=======

                if (role == "provider" && businessType.isBlank()) {
                    toast(context, "Select service type")
                    return@Button
                }

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                if (!isValidUgandaPhone(phone)) {
                    toast(context, "Invalid Uganda phone number")
                    return@Button
                }
<<<<<<< HEAD
=======

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                if (password != confirmPassword) {
                    toast(context, "Passwords do not match")
                    return@Button
                }
<<<<<<< HEAD
                navController.navigate(Routes.TermsAndConditionsScreen.route)
=======

                loading = true

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->

                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        val db = FirebaseFirestore.getInstance()

                        val userData = hashMapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "phone" to phone,
                            "role" to role,
                            "providerType" to if (role == "provider") businessType.lowercase() else "",
                            "subscription" to if (role == "provider") subscription else "",
                            "createdAt" to System.currentTimeMillis()
                        )

                        db.collection("users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener {
                                toast(context, "Account created successfully")
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
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .height(52.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0A9AD9),
                contentColor = Color.White
            )
        ) {
<<<<<<< HEAD
            Text("Continue", fontSize = 18.sp)
=======
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Continue", fontSize = 18.sp)
            }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { navController.navigate(Routes.LoginScreen.route) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
<<<<<<< HEAD

            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) {
                        append("Don’t have an account? ")
=======
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) {
                        append("Already have an account? ")
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    }
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF0A9AD9),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    ) {
                        append("Login")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/* ================= HERO SLIDER ================= */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroImageSlider() {
<<<<<<< HEAD

=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
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
            .height(110.dp)
    ) { page ->
        AsyncImage(
            model = images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

<<<<<<< HEAD
/* ================= COMPONENTS ================= */
=======
/* ================= COMPONENTS & UTILS ================= */
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

@Composable
fun Input(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
<<<<<<< HEAD
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
=======
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
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
fun UgandaPhoneInput(
    phone: String,
    onPhoneChange: (String) -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = phone,
        onValueChange = { onPhoneChange(it.filter { c -> c.isDigit() }.take(9)) },
        label = { Text("Phone Number") },
        leadingIcon = { Text("+256 ") },
        isError = isError,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

<<<<<<< HEAD
/* ================= DROPDOWN (FIXED) ================= */

=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
<<<<<<< HEAD
        modifier = Modifier.zIndex(1f) // ✅ FIX
=======
        modifier = Modifier.zIndex(1f)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
    ) {

        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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

<<<<<<< HEAD

/* ================= ROLE SELECTION ================= */

=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
@Composable
fun RoleSelection(onSelect: (String) -> Unit) {
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
                    Color.Black.copy(alpha = 0.55f)) // 👈 BLUR STRENGTH

        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f)) // ✅ contrast control
=======
            painter = painterResource(id = R.drawable.fuel),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
        )

        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Sign Up As",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
<<<<<<< HEAD
                    onClick = { onSelect("User") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
=======
                    onClick = { onSelect("user") },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                ) {
                    Text("User")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onSelect("provider") },
<<<<<<< HEAD
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A9AD9),
                        contentColor = Color.White
                    )
=======
                    modifier = Modifier.fillMaxWidth().height(48.dp)
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                ) {
                    Text("Service Provider")
                }
            }
        }
    }
}

<<<<<<< HEAD
/* ================= UTILS ================= */

=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
fun isValidUgandaPhone(phone: String): Boolean {
    val prefixes = listOf("70", "74", "75", "76", "77", "78")
    return phone.length == 9 && prefixes.any { phone.startsWith(it) }
}

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
<<<<<<< HEAD
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true, device = "spec:width=360dp,height=640dp")
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
}