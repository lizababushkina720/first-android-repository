package com.example.hw_01_sem2.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.OrganizationModel
import com.example.domain.usecase.GetOrganizationByInnUseCase
import com.example.hw_01_sem2.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val organization: OrganizationModel) : DetailUiState()
    object Error : DetailUiState()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrganizationByInnUseCase: GetOrganizationByInnUseCase
) : ViewModel() {

    private val organizationInn: String = checkNotNull(savedStateHandle[Routes.ARG_ORGANIZATION_INN])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val result = getOrganizationByInnUseCase(organizationInn)
                if (result != null) {
                    _uiState.value = DetailUiState.Success(result)
                } else {
                    _uiState.value = DetailUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error
            }
        }
    }
}