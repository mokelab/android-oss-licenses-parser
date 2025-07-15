package com.mokelab.oss.licenses.parser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LicenseListViewModel @Inject constructor(
    private val parser: Parser
) : ViewModel() {

    sealed interface UiState {
        object Initial : UiState
        object Loading : UiState
        data class Success(val libraries: List<Library>) : UiState
        data class Error(val throwable: Throwable) : UiState
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun load() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val list = parser.parse()
                _uiState.value = UiState.Success(list)
            } catch (e: Throwable) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}