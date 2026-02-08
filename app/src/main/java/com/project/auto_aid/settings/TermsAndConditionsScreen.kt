package com.project.auto_aid.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    navController: NavHostController,
    fromSignup: Boolean = false
) {

    val context = LocalContext.current
    var accepted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms & Conditions") },
                navigationIcon = {
                    // Back button ONLY when opened from Settings
                    if (!fromSignup) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "AutoAid – Terms and Conditions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = TERMS_TEXT,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4
            )

            // 🔐 ENFORCED ONLY AFTER SIGNUP
            if (fromSignup) {

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = accepted,
                        onCheckedChange = { accepted = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("I agree to the Terms & Conditions")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = accepted,
                    onClick = {
                        // ✅ SAVE ACCEPTANCE
                        context.getSharedPreferences(
                            "auto_aid_prefs",
                            Context.MODE_PRIVATE
                        )
                            .edit()
                            .putBoolean("terms_accepted", true)
                            .apply()

                        // ✅ GO TO HOME
                        navController.navigate(Routes.HomeScreen.route) {
                            popUpTo(Routes.SignupScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

private const val TERMS_TEXT = """
LAST UPDATED: JANUARY 2026

1. PLATFORM ROLE
AutoAid is a technology platform that connects users with independent service providers. AutoAid does not directly provide services.

2. USER RESPONSIBILITIES
Users must provide accurate information and ensure their own safety.

3. SERVICE PROVIDERS
Service providers are independent contractors and responsible for their services.

4. PAYMENTS
Payments are between users and providers. AutoAid may charge platform fees.

5. LIABILITY
AutoAid is not liable for damages arising from service delivery.

6. ACCOUNT TERMINATION
Accounts may be suspended for violating these terms.

7. GOVERNING LAW
These terms are governed by the laws of the Republic of Uganda.
"""