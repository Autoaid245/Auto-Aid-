package com.project.auto_aid.provider.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun rememberProfileImagePicker(
    providerId: String,
    onUploaded: (String) -> Unit
): () -> Unit {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri == null) return@rememberLauncherForActivityResult

        val ref = FirebaseStorage.getInstance()
            .reference
            .child("providers/$providerId/profile.jpg")

        ref.putFile(uri)
            .continueWithTask { ref.downloadUrl }
            .addOnSuccessListener { downloadUrl ->

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(providerId)
                    .update("profileImageUrl", downloadUrl.toString())

                onUploaded(downloadUrl.toString())
            }
    }

    return { launcher.launch("image/*") }
}