package com.namma.platform.presentation.map

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.namma.platform.domain.model.*
import com.namma.platform.domain.repository.TrainRepository
import com.namma.platform.domain.usecase.GetCoachLayoutUseCase
import com.namma.platform.domain.usecase.GetTrainPositionUseCase
import com.namma.platform.util.TtsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

data class MapUiState(
    val trainNo: String = "",
    val date: String = "",
    val stationCode: String = "",
    val boardingStation: Station? = null,
    val coachLayout: CoachLayout? = null,
    val trainPosition: TrainPosition? = null,
    val isLoading: Boolean = false,
    val lastUpdatedMinutes: Int = 0,
    val upcomingStations: List<StationEta> = emptyList(),
    val error: String? = null,
    val recenterTrigger: Long = 0L
)

@HiltViewModel
class TrainMapViewModel @Inject constructor(
    private val getTrainPositionUseCase: GetTrainPositionUseCase,
    private val getCoachLayoutUseCase: GetCoachLayoutUseCase,
    private val repository: TrainRepository,
    private val ttsManager: TtsManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trainNo: String = savedStateHandle["trainNo"] ?: ""
    private val date: String = savedStateHandle["date"] ?: ""
    private val stationCode: String = savedStateHandle["stationCode"] ?: ""

    private val _uiState = MutableStateFlow(
        MapUiState(trainNo = trainNo, date = date, stationCode = stationCode)
    )
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadBoardingStationAndCoaches()
        startPolling()
        startLastUpdatedTicker()
    }

    private fun loadBoardingStationAndCoaches() {
        viewModelScope.launch {
            if (stationCode.isNotEmpty()) {
                repository.getStations(stationCode).firstOrNull()?.firstOrNull()?.let { stn ->
                    _uiState.update { it.copy(boardingStation = stn) }
                }
            }

            if (trainNo.isNotEmpty()) {
                val layout = getCoachLayoutUseCase(trainNo)
                _uiState.update { it.copy(coachLayout = layout) }
            }
        }
    }

    private fun startPolling() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTrainPositionUseCase(trainNo, date).collect { result ->
                result.fold(
                    onSuccess = { position ->
                        _uiState.update { state -> 
                            state.copy(
                                trainPosition = position,
                                upcomingStations = position.upcomingStations,
                                lastUpdatedMinutes = 0,
                                isLoading = false,
                                error = null
                            )
                        }
                        checkUpcomingStation(position)
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isLoading = false, error = error.message) }
                    }
                )
            }
        }
    }

    private fun checkUpcomingStation(position: TrainPosition) {
        val currentLatLng = LatLng(position.lastStation.lat, position.lastStation.lng)
        
        position.fullRoute.forEach { routeStn ->
            if (routeStn.status == StationStatus.UPCOMING) {
                val distKm = calculateDistance(
                    currentLatLng.latitude, currentLatLng.longitude,
                    routeStn.station.lat, routeStn.station.lng
                )

                when {
                    distKm <= 5.0 && !routeStn.announced5km -> {
                        triggerAnnouncement(routeStn.station, "APPROACHING")
                        routeStn.announced5km = true
                    }
                    distKm <= 2.0 && !routeStn.announced2km -> {
                        triggerAnnouncement(routeStn.station, "REMINDER")
                        routeStn.announced2km = true
                    }
                    distKm <= 0.3 && !routeStn.announcedArrival -> {
                        triggerAnnouncement(routeStn.station, "ARRIVAL")
                        routeStn.announcedArrival = true
                    }
                }
            }
        }
    }

    private fun triggerAnnouncement(station: Station, type: String) {
        // Here we can use the specialized methods in TtsManager
        when (type) {
            "APPROACHING" -> ttsManager.announceArrival(station.nameEnglish, 0)
            "REMINDER" -> ttsManager.announceArrival(station.nameEnglish, 0)
            "ARRIVAL" -> ttsManager.announceArrival(station.nameEnglish, 0)
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000.0
    }

    private fun startLastUpdatedTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1.minutes)
                _uiState.update { 
                    it.copy(lastUpdatedMinutes = it.lastUpdatedMinutes + 1)
                }
            }
        }
    }

    fun onRecenterTapped() {
        _uiState.update { it.copy(recenterTrigger = System.currentTimeMillis()) }
    }
}
