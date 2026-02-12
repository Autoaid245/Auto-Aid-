package com.project.auto_aid.provider.model

data class Provider(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val providerType: String = "",
    val rating: Double = 0.0,
    val isOnline: Boolean = true
)