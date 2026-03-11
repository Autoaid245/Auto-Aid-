package com.project.auto_aid.data.network.dto
import com.project.auto_aid.data.network.dto.UserLocationBody
data class CreateRequestBody(
    val service: String,
    val providerType: String,
    val vehicleInfo: String,
    val problem: String,
    val towType: String,
    val userLocation: UserLocationBody,
    val targetProviderId: String? = null
)