package com.project.auto_aid.screens.towing

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TowingViewModel : ViewModel() {

    private val _request = MutableStateFlow(
        TowingRequest(
            id = "REQ-001",
            status = TowingStatus.REQUEST_SENT
        )
    )
    val request: StateFlow<TowingRequest> = _request

    init {
        simulateProgress()
    }

    private fun simulateProgress() {
        viewModelScope.launch {
            delay(3000)
            _request.update {
                it.copy(
                    status = TowingStatus.DRIVER_ASSIGNED,
                    driver = Driver(
                        name = "John Doe",
                        phone = "+256700000000",
                        truckPlate = "UBF 234A",
                        truckType = "Flatbed",
                        rating = 4.6
                    )
                )
            }

            delay(4000)
            _request.update { it.copy(status = TowingStatus.DRIVER_ON_THE_WAY) }

            delay(4000)
            _request.update { it.copy(status = TowingStatus.ARRIVED) }

            delay(3000)
            _request.update { it.copy(status = TowingStatus.VEHICLE_TOWED) }

            delay(3000)
            _request.update { it.copy(status = TowingStatus.COMPLETED) }
        }
    }

    fun cancelRequest() {
        _request.update { it.copy(status = TowingStatus.CANCELLED) }
    }
}
