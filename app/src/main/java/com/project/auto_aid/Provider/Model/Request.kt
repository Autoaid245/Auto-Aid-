package com.project.auto_aid.provider.model

data class Request(
    val id: String = "",
    val status: String = "",
    val providerType: String = "",
    val assignedProviderId: String = "",
    val userLocation: Map<String, Double>? = null,
    val providerLocation: Map<String, Double>? = null
)