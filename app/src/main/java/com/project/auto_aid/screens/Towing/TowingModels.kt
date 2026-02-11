package com.project.auto_aid.screens.towing

data class Driver(
    val name: String,
    val phone: String,
    val truckPlate: String,
    val truckType: String,
    val rating: Double
)

data class TowingRequest(
    val id: String,
    val status: TowingStatus,
    val driver: Driver? = null
)
