package com.project.auto_aid.provider

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.project.auto_aid.provider.model.Request

class ProviderRepository {

    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null

    fun listenRequests(
        providerType: String,
        providerId: String,
        onUpdate: (List<Request>) -> Unit
    ) {
        listener?.remove()

        listener = db.collection("requests")
            .whereEqualTo("providerType", providerType)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents
                    ?.mapNotNull { it.toObject(Request::class.java)?.copy(id = it.id) }
                    ?.filter {
                        it.status == "pending" || it.assignedProviderId == providerId
                    } ?: emptyList()

                onUpdate(list)
            }
    }

    fun assignRequest(requestId: String, providerId: String) {
        val ref = db.collection("requests").document(requestId)
        db.runTransaction {
            val snap = it.get(ref)
            if (snap.getString("status") == "pending") {
                it.update(ref, mapOf(
                    "status" to "assigned",
                    "assignedProviderId" to providerId
                ))
            }
        }
    }

    fun updateStatus(requestId: String, status: String) {
        db.collection("requests").document(requestId)
            .update("status", status)
    }

    fun listenUserLocation(
        requestId: String,
        onUpdate: (Double, Double) -> Unit
    ) {
        db.collection("requests").document(requestId)
            .addSnapshotListener { snap, _ ->
                val lat = snap?.getDouble("userLocation.lat")
                val lng = snap?.getDouble("userLocation.lng")
                if (lat != null && lng != null) onUpdate(lat, lng)
            }
    }
}