package com.project.auto_aid.data.network.dto

data class LocationDto(
    val lat: Double? = 0.0,
    val lng: Double? = 0.0
)

data class RequestDto(
    val _id: String? = null,
    val id: String? = null,

    val status: String? = null,
    val providerType: String? = null,

    val targetProviderId: String? = null,

    val assignedProviderId: String? = null,
    val assignedProviderName: String? = null,
    val assignedProviderPhone: String? = null,
    val assignedProviderRating: Double? = null,

    // ✅ ADD THESE (so ProviderRepository can read them)
    val service: String? = null,
    val vehicleInfo: String? = null,
    val problem: String? = null,
    val towType: String? = null,

    val userLocation: LocationDto? = null
) {
    fun resolvedId(): String = _id ?: id ?: ""
}