package pe.edu.upc.saludya.features.patientreservations.presentation.beneficiario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetBeneficiariesUseCase
import pe.edu.upc.saludya.features.patientreservations.domain.model.Beneficiary
import javax.inject.Inject

@HiltViewModel
class BeneficiarioViewModel @Inject constructor(
    private val getBeneficiaries: GetBeneficiariesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BeneficiarioUiState())
    val uiState: StateFlow<BeneficiarioUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun onSelect(beneficiary: BeneficiarioUi) {
        _uiState.update { it.copy(selectedId = beneficiary.id) }
    }

    private fun load() {
        viewModelScope.launch {
            runCatching { getBeneficiaries() }
                .onSuccess { beneficiaries ->
                    val items = beneficiaries.map { it.toUi() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            beneficiaries = items,
                            selectedId = items.firstOrNull()?.id
                        )
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

private fun Beneficiary.toUi() = BeneficiarioUi(
    id = patientId,
    name = displayName,
    titular = titular,
    maskedDni = maskedDni
)
