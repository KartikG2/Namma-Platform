package com.namma.platform.presentation.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.usecase.SaveRecentStationUseCase
import com.namma.platform.domain.usecase.SearchStationsUseCase
import com.namma.platform.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class StationSelectionViewModel @Inject constructor(
    private val searchStationsUseCase: SearchStationsUseCase,
    private val saveRecentStationUseCase: SaveRecentStationUseCase,
    private val repo: TrainRepository // to get recent stations
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow(StationUiState())
    val uiState: StateFlow<StationUiState> = _uiState.asStateFlow()

    init {
        // Load recent stations
        repo.getRecentStations().onEach { recents ->
            _uiState.update { it.copy(recentStations = recents) }
        }.launchIn(viewModelScope)

        // Debounce search query
        _searchQuery
            .debounce(300)
            .onEach { query ->
                _uiState.update { it.copy(isLoading = true) }
                searchStationsUseCase(query).collect { results ->
                    _uiState.update { it.copy(searchResults = results, isLoading = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onStationSelected(station: Station) {
        viewModelScope.launch {
            saveRecentStationUseCase(station)
        }
    }
}
