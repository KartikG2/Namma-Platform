package com.namma.platform.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.SearchHistoryItem
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.repository.TrainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val fromStation: Station? = null,
    val toStation: Station? = null,
    val isLoading: Boolean = false,
    val searchHistory: List<SearchHistoryItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class TrainSearchViewModel @Inject constructor(
    private val repository: TrainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        // Mocking search history for now, implementation would use repository
        repository.getSearchHistory().onEach { history ->
            _uiState.update { it.copy(searchHistory = history) }
        }.launchIn(viewModelScope)
    }

    fun onFromStationSelected(station: Station) {
        _uiState.update { it.copy(fromStation = station) }
    }

    fun onToStationSelected(station: Station) {
        _uiState.update { it.copy(toStation = station) }
    }

    fun clearFromStation() {
        _uiState.update { it.copy(fromStation = null) }
    }

    fun clearToStation() {
        _uiState.update { it.copy(toStation = null) }
    }

    fun swapStations() {
        _uiState.update { state ->
            state.copy(
                fromStation = state.toStation,
                toStation = state.fromStation
            )
        }
    }

    fun onHistoryItemTapped(item: SearchHistoryItem) {
        _uiState.update { state ->
            state.copy(
                fromStation = item.fromStation,
                toStation = item.toStation
            )
        }
    }

    fun onFindTrainsClicked(onSearch: (String, String) -> Unit) {
        val state = _uiState.value
        if (state.fromStation != null && state.toStation != null) {
            if (state.fromStation.code == state.toStation.code) {
                _uiState.update { it.copy(error = "ಮೂಲ ಮತ್ತು ಗಮ್ಯಸ್ಥಾನ ಒಂದೇ ಆಗಿರಬಾರದು / Source and destination cannot be same") }
            } else {
                onSearch(state.fromStation.code, state.toStation.code)
                // Save to history (logic in repository)
                viewModelScope.launch {
                    repository.saveSearchHistory(
                        SearchHistoryItem(
                            fromStation = state.fromStation,
                            toStation = state.toStation
                        )
                    )
                }
            }
        }
    }
}
