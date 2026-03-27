package com.example.hw_01_sem2.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.OrganizationModel
import com.example.domain.usecase.GetOrganizationsByQueryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(
        val organizations: List<OrganizationModel>,
        val fromCache: Boolean
    ) : SearchUiState()
    data class Error(val messageKey: String) : SearchUiState()
}

sealed class SearchSnackbarEvent {
    object DataFromCache : SearchSnackbarEvent()
    object DataFromServer : SearchSnackbarEvent()
    data class Error(val messageKey: String) : SearchSnackbarEvent()
}

class SearchViewModel(
    private val getOrganizationsByQueryUseCase: GetOrganizationsByQueryUseCase
) : ViewModel() {

    companion object {
        const val UNKNOWN_ERROR = "unknown_error"
    }

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _snackbarEvent = MutableStateFlow<SearchSnackbarEvent?>(null)
    val snackbarEvent: StateFlow<SearchSnackbarEvent?> = _snackbarEvent.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun clearQuery() {
        _query.value = ""
    }

    fun performSearch() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isEmpty()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            try {
                val (organizations, fromCache) = getOrganizationsByQueryUseCase(currentQuery)

                _uiState.value = SearchUiState.Success(organizations, fromCache)

                _snackbarEvent.value = if (fromCache) {
                    SearchSnackbarEvent.DataFromCache
                } else {
                    SearchSnackbarEvent.DataFromServer
                }

            } catch (e: Exception) {
                val errorKey = e.message ?: UNKNOWN_ERROR
                _uiState.value = SearchUiState.Error(errorKey)
                _snackbarEvent.value = SearchSnackbarEvent.Error(errorKey)
            }
        }
    }

    fun clearSnackbarEvent() {
        _snackbarEvent.value = null
    }
}