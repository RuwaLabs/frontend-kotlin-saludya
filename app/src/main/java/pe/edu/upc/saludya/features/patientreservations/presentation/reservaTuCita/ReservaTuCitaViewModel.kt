package pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetAllSpecialitiesUseCase
import javax.inject.Inject

@HiltViewModel
class ReservaTuCitaViewModel @Inject constructor(
    private val getAllSpecialities: GetAllSpecialitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservaTuCitaUiState(isLoading = true))
    val uiState: StateFlow<ReservaTuCitaUiState> = _uiState.asStateFlow()

    init {
        loadSpecialities()
    }

    fun onQueryChange(query: String) {
        _uiState.update { state ->
            val updated = state.copy(query = query)
            updated.copy(
                navigateToNoResults = !updated.isLoading &&
                    updated.errorMessage == null &&
                    query.isNotBlank() &&
                    updated.visibleSpecialities.isEmpty()
            )
        }
    }

    fun onNoResultsHandled() {
        _uiState.update { it.copy(navigateToNoResults = false) }
    }

    private fun loadSpecialities() {
        viewModelScope.launch {
            runCatching { getAllSpecialities() }
                .onSuccess { specialities ->
                    _uiState.update {
                        it.copy(isLoading = false, specialities = specialities, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: ""
                        )
                    }
                }
        }
    }
}
