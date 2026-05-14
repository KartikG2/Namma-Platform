package com.namma.platform.presentation.station

import com.namma.platform.domain.model.Station

/**
 * UI state for the Station Selection screen.
 */
data class StationUiState(
    val searchQuery: String = "",
    val searchResults: List<Station> = emptyList(),
    val recentStations: List<Station> = emptyList(),
    val favoriteStations: List<Station> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
