package com.project.auto_aid.settings

<<<<<<< HEAD
=======
import android.widget.Toast
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
=======
import androidx.compose.ui.platform.LocalContext
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
<<<<<<< HEAD
=======
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
import com.project.auto_aid.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    navController: NavController,
<<<<<<< HEAD
    fromSignup: Boolean = false
) {

    var accepted by remember { mutableStateOf(false) }
=======
    fromSignup: Boolean = false,

    // 🔽 THESE COME FROM SIGNUP (DO NOT CHANGE UI)
    name: String = "",
    email: String = "",
    phone: String = "",
    password: String = "",
    role: String = ""
) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var accepted by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Terms & Conditions",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            /* ================= TERMS CONTENT ================= */

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Text(
                    text = "Welcome to AutoAid",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = """
                        By using AutoAid, you agree to the following terms and conditions.
                        
                        1. AutoAid provides roadside assistance services including towing, fuel delivery, garage assistance, and emergency services.
                        
                        2. Users must provide accurate personal and vehicle information.
                        
                        3. Payments are processed securely and are non-refundable once a service is completed.
                        
                        4. Service providers are independent contractors; AutoAid is not liable for damages caused during service delivery.
                        
                        5. Misuse of the platform may result in account suspension.
                        
                        6. Your data is handled according to our Privacy Policy.
<<<<<<< HEAD
                        
                        Please read carefully before accepting.
=======
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    """.trimIndent(),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

<<<<<<< HEAD
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
=======
                Row(verticalAlignment = Alignment.CenterVertically) {
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    Checkbox(
                        checked = accepted,
                        onCheckedChange = { accepted = it }
                    )
<<<<<<< HEAD

                    Spacer(modifier = Modifier.width(8.dp))

=======
                    Spacer(modifier = Modifier.width(8.dp))
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    Text(
                        text = "I have read and agree to the Terms & Conditions",
                        fontSize = 14.sp
                    )
                }
            }

            /* ================= ACTION BUTTONS ================= */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Button(
                    onClick = {
<<<<<<< HEAD
                        if (fromSignup) {
                            navController.navigate(Routes.SignupScreen.route) {
                                popUpTo(Routes.TermsAndConditionsScreen.route) {
                                    inclusive = true
                                }
                            }
=======
                        if (!accepted || loading) return@Button

                        // 🔥 THIS IS THE FIX
                        if (fromSignup) {

                            loading = true

                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener { result ->

                                    val uid = result.user?.uid ?: return@addOnSuccessListener

                                    val userData = hashMapOf(
                                        "name" to name,
                                        "email" to email,
                                        "phone" to phone,
                                        "role" to role.lowercase(), // ✅ CRITICAL
                                        "createdAt" to System.currentTimeMillis()
                                    )

                                    db.collection("users")
                                        .document(uid)
                                        .set(userData)
                                        .addOnSuccessListener {

                                            loading = false

                                            // ✅ Navigate by role
                                            when (role.lowercase()) {
                                                "user" ->
                                                    navController.navigate(Routes.HomeScreen.route) {
                                                        popUpTo(0)
                                                    }

                                                "provider" ->
                                                    navController.navigate(Routes.GarageScreen.route) {
                                                        popUpTo(0)
                                                    }
                                            }
                                        }
                                        .addOnFailureListener {
                                            loading = false
                                            Toast.makeText(
                                                context,
                                                "Failed to save user data",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                .addOnFailureListener {
                                    loading = false
                                    Toast.makeText(
                                        context,
                                        it.localizedMessage ?: "Signup failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                        } else {
                            navController.popBackStack()
                        }
                    },
<<<<<<< HEAD
                    enabled = accepted,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("ACCEPT")
=======
                    enabled = accepted && !loading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (loading) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    } else {
                        Text("ACCEPT")
                    }
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
<<<<<<< HEAD
                    onClick = {
                        navController.popBackStack()
                    },
=======
                    onClick = { navController.popBackStack() },
>>>>>>> 5171f649971e06d49a39c0ca5de78ab0caa9fea0
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("DECLINE")
                }
            }
        }
    }
}