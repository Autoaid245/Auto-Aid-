package com.project.auto_aid.provider

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.project.auto_aid.provider.model.Request

class ProviderViewModel : ViewModel() {

    private val repo = ProviderRepository()

    val pending = mutableStateListOf<Request>()
    val ongoing = mutableStateListOf<Request>()
    val completed = mutableStateListOf<Request>()

    fun start(providerType: String, providerId: String) {
        repo.listenRequests(providerType, providerId) { list ->
            pending.clear()
            ongoing.clear()
            completed.clear()

            list.forEach {
                when (it.status.lowercase()) {
                    "pending" -> pending.add(it)
                    "completed" -> completed.add(it)
                    else -> ongoing.add(it)
                }
            }
        }
    }

    fun accept(requestId: String, providerId: String) {
        repo.assignRequest(requestId, providerId)
    }

    fun updateStatus(requestId: String, status: String) {
        repo.updateStatus(requestId, status)
    }

    fun listenUserLocation(
        requestId: String,
        onUpdate: (Double, Double) -> Unit
    ) {
        repo.listenUserLocation(requestId, onUpdate)
    }
}