package com.project.auto_aid.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes
import kotlinx.coroutines.delay

/* ================= SIGNUP SCREEN ================= */

@Composable
fun SignupScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var role by remember { mutableStateOf("") } // change if needed
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var businessType by remember { mutableStateOf("") }
    var subscription by remember { mutableStateOf("") }

    if (role.isEmpty()) {
        RoleSelection { selectedRole ->
            role = selectedRole
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

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Fast help at your location",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Input("Full Name", name) { name = it }
                Input("Email Address", email) { email = it }

                UgandaPhoneInput(
                    phone = phone,
                    onPhoneChange = { phone = it },
                    isError = phone.isNotEmpty() && !isValidUgandaPhone(phone)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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

                if (role.equals("provider", ignoreCase = true)) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
            onClick = {
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

                // ✅ Providers must select extra fields
                if (role.equals("provider", true) && businessType.isBlank()) {
                    toast(context, "Select Service Type")
                    return@Button
                }
                if (role.equals("provider", true) && subscription.isBlank()) {
                    toast(context, "Select Subscription")
                    return@Button
                }

                // ✅ Create Firebase Auth account
                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: run {
                            toast(context, "Signup failed (no uid)")
                            return@addOnSuccessListener
                        }

                        // ✅ Save extra data in Firestore
                        val userData = hashMapOf(
                            "name" to name.trim(),
                            "email" to email.trim(),
                            "phone" to phone.trim(),
                            "role" to role.lowercase(),
                            "providerType" to if (role.equals("provider", true)) businessType.lowercase() else "",
                            "subscription" to if (role.equals("provider", true)) subscription.lowercase() else "",
                            "rating" to 0.0,
                            "profileImageUrl" to "",
                            "isOnline" to false
                        )

                        db.collection("users").document(uid).set(userData)
                            .addOnSuccessListener {
                                toast(context, "Account created successfully ✅")
                                navController.navigate(Routes.LoginScreen.route) {
                                    popUpTo(Routes.SignupScreen.route) { inclusive = true }
                                }
                            }
                            .addOnFailureListener { e ->
                                toast(context, e.message ?: "Failed to save user info")
                            }
                    }
                    .addOnFailureListener { e ->
                        toast(context, e.message ?: "Signup failed")
                    }
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
            Text(
                text = "Continue", fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { navController.navigate(Routes.LoginScreen.route) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
                        append(" Login")
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

/* ================= COMPONENTS ================= */

@Composable
fun Input(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
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
           IconButton(onClick = { toggle()}) {
               Icon(
                   painter = painterResource(
                       id = if (show) R.drawable.no_see else R.drawable.see
                   ),
                   contentDescription = if (show) "Hide Password"
                   else "Show Password",
                   modifier = Modifier.size(25.dp)
               )
           }
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

/* ================= DROPDOWN (FIXED) ================= */

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
        modifier = Modifier.zIndex(1f)
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
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFD6ECFF)) // light blue menu
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

/* ================= ROLE SELECTION ================= */

@Composable
fun RoleSelection(onSelect: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
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

                Spacer(modifier = Modifier.height(8.dp))

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

/* ================= UTILS ================= */

fun isValidUgandaPhone(phone: String): Boolean {
    val prefixes = listOf("70", "74", "75", "76", "77", "78")
    return phone.length == 9 && prefixes.any { phone.startsWith(it) }
}

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true, device = "spec:width=360dp,height=640dp")
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}