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
import androidx.compose.ui.draw.blur
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
import coil.compose.AsyncImage
import com.project.auto_aid.R
import com.project.auto_aid.navigation.Routes   // ✅ IMPORTANT
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

/* ================= SIGNUP SCREEN ================= */

@Composable
fun SignupScreen(navController: NavController) {

    val context = LocalContext.current

    var role by remember { mutableStateOf<String?>(null) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    var businessType by remember { mutableStateOf("") }
    var subscription by remember { mutableStateOf("monthly") }

    /* ===== ROLE SELECTION FIRST ===== */
    if (role == null) {
        RoleSelection { role = it }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeroImageSlider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create Account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Fast help at your location",
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Box(Modifier.weight(1f)) {
                        PasswordInput(
                            label = "Password",
                            value = password,
                            show = showPassword,
                            toggle = { showPassword = !showPassword }
                        ) { password = it }
                    }

                    Box(Modifier.weight(1f)) {
                        PasswordInput(
                            label = "Confirm",
                            value = confirmPassword,
                            show = showConfirm,
                            toggle = { showConfirm = !showConfirm }
                        ) { confirmPassword = it }
                    }
                }

                if (role.equals("provider", true)) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Box(Modifier.weight(1f)) {
                            Dropdown(
                                label = "Service Type",
                                options = listOf("Garage", "Fuel", "Towing", "Ambulance"),
                                selected = businessType
                            ) { businessType = it }
                        }

                        Box(Modifier.weight(1f)) {
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

        Spacer(modifier = Modifier.height(12.dp))

        /* ===== CONTINUE → TERMS ===== */

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

                // ✅ CORRECT ROUTE — THIS FIXES THE CRASH
                navController.navigate(
                    Routes.TermsAndConditionsScreen.route + "?fromSignup=true"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Continue", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))
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
            .height(120.dp)
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
fun RoleSelection(onSelect: (String) -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.fuel),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
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
                    text = "Sign Up As",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSelect("user") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("User")
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onSelect("provider") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Service Provider")
                }
            }
        }
    }
}

/* ================= HELPERS ================= */

@Composable
fun Input(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
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
        onValueChange = { onPhoneChange(it.filter(Char::isDigit).take(9)) },
        label = { Text("Phone Number") },
        leadingIcon = { Text("+256 ") },
        isError = isError,
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
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        visualTransformation = if (show) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = toggle) {
                Text(if (show) "🙈" else "👁️")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

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
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
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

fun isValidUgandaPhone(phone: String): Boolean =
    phone.length == 9 &&
            listOf("70", "74", "75", "76", "77", "78").any { phone.startsWith(it) }

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(navController = rememberNavController())
}