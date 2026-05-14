package com.namma.platform.presentation.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(
    private val repository: TrainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StationUiState())
    val uiState: StateFlow<StationUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPopularStations()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        // Debounce search
        searchJob?.cancel()
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300) // debounce
                searchStations(query)
            }
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    private suspend fun searchStations(query: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            repository.getStations(query).collect { results ->
                _uiState.update { it.copy(searchResults = results, isLoading = false) }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Search failed"
                )
            }
        }
    }

    private fun loadPopularStations() {
        // Provide hardcoded popular stations as initial data
        val popular = listOf(
            Station("SBC", "ಕೆ.ಎಸ್.ಆರ್ ಬೆಂಗಳೂರು", "KSR Bengaluru City Jn", 12.9733, 77.5670, "SWR"),
            Station("MAS", "ಚೆನ್ನೈ ಸೆಂಟ್ರಲ್", "Chennai Central", 13.0827, 80.2707, "SR"),
            Station("NDLS", "ನವದೆಹಲಿ", "New Delhi", 28.6415, 77.2197, "NR"),
            Station("CSTM", "ಮುಂಬೈ ಸಿ.ಎಸ್.ಟಿ", "Mumbai CSMT", 18.9400, 72.8353, "CR"),
            Station("HWH", "ಹೌರಾ", "Howrah Junction", 22.5837, 88.3415, "ER"),
            Station("SC", "ಸಿಕಂದರಾಬಾದ್", "Secunderabad Jn", 17.4334, 78.5017, "SCR"),
            Station("BZA", "ವಿಜಯವಾಡ", "Vijayawada Jn", 16.5185, 80.6201, "SCR"),
            Station("JP", "ಜೈಪುರ", "Jaipur Junction", 26.9197, 75.7878, "NWR")
        )
        _uiState.update { it.copy(recentStations = popular) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
