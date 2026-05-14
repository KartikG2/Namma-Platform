package com.namma.platform.presentation.coach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namma.platform.domain.model.CoachLayout
import com.namma.platform.domain.usecase.GetCoachLayoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoachLayoutUiState(
    val trainNo: String = "",
    val coachLayout: CoachLayout? = null,
    val isLoading: Boolean = false,
    val selectedCoachIndex: Int? = null
)

@HiltViewModel
class CoachLayoutViewModel @Inject constructor(
    private val getCoachLayoutUseCase: GetCoachLayoutUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trainNo: String = savedStateHandle["trainNo"] ?: ""
    private val _uiState = MutableStateFlow(CoachLayoutUiState(trainNo = trainNo))
    val uiState: StateFlow<CoachLayoutUiState> = _uiState.asStateFlow()

    init {
        loadCoaches()
    }

    private fun loadCoaches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val layout = getCoachLayoutUseCase(trainNo)
            _uiState.update { it.copy(coachLayout = layout, isLoading = false) }
        }
    }

    fun onCoachTapped(index: Int) {
        _uiState.update { it.copy(selectedCoachIndex = index) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedCoachIndex = null) }
    }
}
