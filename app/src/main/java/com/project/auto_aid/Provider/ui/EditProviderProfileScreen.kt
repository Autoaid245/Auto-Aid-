package com.project.auto_aid.provider.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProviderProfileScreen(
    navController: NavHostController
) {
    val providerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()


    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var serviceArea by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }

    /* IMAGE PICKER */
    val uploadImage = rememberProfileImagePicker(providerId) { newUrl ->
        profileImageUrl = newUrl
    }

    /* LOAD DATA */
    LaunchedEffect(Unit) {
        db.collection("users").document(providerId).get()
            .addOnSuccessListener {
                name = it.getString("name") ?: ""
                phone = it.getString("phone") ?: ""
                bio = it.getString("bio") ?: ""
                serviceArea = it.getString("serviceArea") ?: ""
                profileImageUrl = it.getString("profileImageUrl") ?: ""
                loading = false
            }
            .addOnFailureListener { loading = false }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            /* ---------- AVATAR ---------- */
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {

                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { uploadImage() }
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { uploadImage() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            /* ---------- FORM ---------- */

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    // allow only digits and +
                    phone = it.filter { ch -> ch.isDigit() || ch == '+' }
                },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = serviceArea,
                onValueChange = { serviceArea = it },
                label = { Text("Service Area (e.g. Kampala, Wakiso)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))



            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    saving = true

                    db.collection("users").document(providerId)
                        .update(
                            mapOf(
                                "name" to name.trim(),
                                "phone" to phone.trim(),
                                "bio" to bio.trim(),
                                "serviceArea" to serviceArea.trim(),
                                "profileImageUrl" to profileImageUrl
                            )
                        )
                        .addOnSuccessListener {
                            saving = false
                            navController.popBackStack()
                        }
                        .addOnFailureListener {
                            saving = false
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !saving
            ) {

                if (saving)
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                else
                    Text("Save Changes")
            }
        }
    }


}
