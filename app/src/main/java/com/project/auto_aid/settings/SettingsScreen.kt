package com.project.auto_aid.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.auto_aid.navigation.Routes
import com.project.auto_aid.screens.AppBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        bottomBar = { AppBottomNavigationBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            /* ================= MY ACCOUNT ================= */

            SectionTitle("My Account")

            SectionCard {

                SettingsRow("Profile", Icons.Default.Person) {
                    navController.navigate(Routes.UserInfoScreen.route)
                }

                SettingsRow("Promotion", Icons.Default.Book) {
                    navController.navigate(Routes.PromotionScreen.route)
                }

                SettingsRow("Payout Information", Icons.Default.AccountBalanceWallet) {
                    navController.navigate(Routes.PayoutInformationScreen.route)
                }

                SettingsRow("About Us", Icons.Default.Info) {
                    navController.navigate(Routes.AboutUsScreen.route)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ================= INFORMATION ================= */

            SectionTitle("Information")

            SectionCard {

                SettingsRow(
                    title = "Version",
                    icon = Icons.Default.PhoneAndroid,
                    trailingText = "1.0.0"
                )

                SettingsRow(
                    title = "Terms of Service",
                    icon = Icons.Default.Description
                ) {
                    navController.navigate(Routes.TermsAndConditionsScreen.route)
                }

                SettingsRow(
                    title = "Privacy Policy",
                    icon = Icons.Default.Security
                ) {
                    navController.navigate(Routes.PrivacyPolicyScreen.route)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            /* ================= SIGN OUT ================= */

            Text(
                text = "Sign Out",
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(0)
                        }
                    }
                    .padding(vertical = 18.dp)
            )
        }
    }
}

/* ---------------------------------------------------
   REUSABLE COMPONENTS
---------------------------------------------------- */

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column { content() }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    icon: ImageVector,
    trailingText: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        trailingText?.let {
            Text(
                text = it,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black
        )
    }

    Divider(modifier = Modifier.padding(start = 16.dp))
}