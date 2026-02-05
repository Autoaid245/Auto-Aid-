package com.project.auto_aid.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

private val AppBlue = Color(0xFF0A9AD8)
private val SoftBg = Color(0xFFF6F8FB)

enum class PayoutType { MOBILE_MONEY, BANK }
enum class MobileProvider { MTN, AIRTEL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayoutInformationScreen(navController: NavHostController) {

    var editMode by remember { mutableStateOf(false) }

    /* -------- PAYOUT TYPE -------- */
    var payoutType by remember { mutableStateOf(PayoutType.MOBILE_MONEY) }

    /* -------- MOBILE MONEY -------- */
    var mobileProvider by remember { mutableStateOf(MobileProvider.MTN) }
    var phoneNumber by remember { mutableStateOf("+256 700 000 000") }

    /* -------- BANK -------- */
    var bankName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }

    var accountName by remember { mutableStateOf("Ssentongo Nicholas") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Payout Information") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    if (editMode) {
                        TextButton(onClick = { editMode = false }) {
                            Text("Save", color = AppBlue, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        IconButton(onClick = { editMode = true }) {
                            Icon(Icons.Default.Edit, null)
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(SoftBg)
                .padding(16.dp)
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    /* -------- VIEW MODE -------- */
                    if (!editMode) {

                        PayoutInfoRow(
                            "Payout Method",
                            if (payoutType == PayoutType.MOBILE_MONEY) "Mobile Money" else "Bank Account"
                        )

                        if (payoutType == PayoutType.MOBILE_MONEY) {
                            PayoutInfoRow("Provider", mobileProvider.name)
                            PayoutInfoRow("Phone Number", phoneNumber)
                        } else {
                            PayoutInfoRow("Bank Name", bankName)
                            PayoutInfoRow("Account Number", accountNumber)
                        }

                        PayoutInfoRow("Account Name", accountName)
                    }

                    /* -------- EDIT MODE -------- */
                    else {

                        Text("Payout Method", fontWeight = FontWeight.Medium)

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilterChip(
                                selected = payoutType == PayoutType.MOBILE_MONEY,
                                onClick = { payoutType = PayoutType.MOBILE_MONEY },
                                label = { Text("Mobile Money") }
                            )
                            FilterChip(
                                selected = payoutType == PayoutType.BANK,
                                onClick = { payoutType = PayoutType.BANK },
                                label = { Text("Bank Account") }
                            )
                        }

                        if (payoutType == PayoutType.MOBILE_MONEY) {

                            Text("Mobile Provider", fontWeight = FontWeight.Medium)

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                FilterChip(
                                    selected = mobileProvider == MobileProvider.MTN,
                                    onClick = { mobileProvider = MobileProvider.MTN },
                                    label = { Text("MTN") }
                                )
                                FilterChip(
                                    selected = mobileProvider == MobileProvider.AIRTEL,
                                    onClick = { mobileProvider = MobileProvider.AIRTEL },
                                    label = { Text("Airtel") }
                                )
                            }

                            PayoutEditField("Phone Number", phoneNumber) {
                                phoneNumber = it
                            }
                        }

                        if (payoutType == PayoutType.BANK) {
                            PayoutEditField("Bank Name", bankName) {
                                bankName = it
                            }
                            PayoutEditField("Account Number", accountNumber) {
                                accountNumber = it
                            }
                        }

                        PayoutEditField("Account Name", accountName) {
                            accountName = it
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Payments are sent securely to your selected payout method.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

/* ---------------- HELPERS ---------------- */

@Composable
private fun PayoutInfoRow(label: String, value: String) {
    Column {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PayoutEditField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    Column {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}