package com.namma.platform.presentation.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.usecase.GetNextTrainsUseCase
import com.namma.platform.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val trains: List<Train> = emptyList(),
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val selectedStation: Station? = null,
    val error: String? = null,
    val stationCode: String = "" // Added to support previous logic if needed
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getNextTrainsUseCase: GetNextTrainsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fromCode: String = savedStateHandle["fromCode"] ?: ""
    private val toCode: String = savedStateHandle["toCode"] ?: ""
    private val _uiState = MutableStateFlow(DashboardUiState(stationCode = fromCode))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Observe network state
        NetworkUtil.isOnline.onEach { online ->
            _uiState.update { it.copy(isOffline = !online) }
        }.launchIn(viewModelScope)

        loadTrains()
    }

    fun loadTrains() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getNextTrainsUseCase(fromCode, toCode).collect { result ->
                result.fold(
                    onSuccess = { trains ->
                        _uiState.update { it.copy(trains = trains, isLoading = false) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(error = error.message, isLoading = false) }
                    }
                )
            }
        }
    }

    fun onRefresh() {
        loadTrains()
    }

    fun onHelpMeTapped(train: Train, platformNo: Int, ttsManager: com.namma.platform.util.TtsManager) {
        if (train.delayMinutes > 0) {
            ttsManager.announceDelay(train)
        } else {
            // For general help, we announce the platform arrival/departure
            ttsManager.announcePlatform(train, "ಬೆಂಗಳೂರು", "ಮೈಸೂರು") // Placeholders for origin/destination
        }
    }

}
