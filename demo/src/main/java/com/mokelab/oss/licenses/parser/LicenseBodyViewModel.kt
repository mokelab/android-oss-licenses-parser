package com.mokelab.oss.licenses.parser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LicenseBodyViewModel.Factory::class)
class LicenseBodyViewModel @AssistedInject constructor(
    private val parser: Parser,
    @Assisted("offset") val offset: Int,
    @Assisted("length") val length: Int,
    @Assisted("name") val name: String,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("offset") offset: Int,
            @Assisted("length") length: Int,
            @Assisted("name") name: String,
        ): LicenseBodyViewModel
    }

    private val library = Library(
        offset = offset,
        length = length,
        name = name,
    )

    sealed interface UiState {
        object Initial : UiState
        object Loading : UiState
        data class Success(val body: String) : UiState
        data class Error(val throwable: Throwable) : UiState
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun load() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val body = parser.loadBody(library)
                _uiState.value = UiState.Success(body)
            } catch (e: Throwable) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}