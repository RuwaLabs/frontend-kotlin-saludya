package pe.edu.upc.saludya.features.patientreservations.presentation.elegirProfesional

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetBeneficiaryByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetDoctorsBySpecialityIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ElegirProfesionalViewModel @Inject constructor(
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getDoctorsBySpecialityId: GetDoctorsBySpecialityIdUseCase,
    private val getBeneficiaryById: GetBeneficiaryByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElegirProfesionalUiState(isLoading = true))
    val uiState: StateFlow<ElegirProfesionalUiState> = _uiState.asStateFlow()

    private var started = false

    fun start(specialityId: Int, beneficiaryId: Int) {
        if (started) return
        started = true
        viewModelScope.launch {
            runCatching {
                val specialityName = getSpecialityById(specialityId)?.name.orEmpty()
                val doctors = getDoctorsBySpecialityId(specialityId)
                val beneficiaryName = runCatching { getBeneficiaryById(beneficiaryId) }
                    .getOrNull()?.displayName.orEmpty()
                specialityName to doctors.map { doctor ->
                    ProfesionalUi(
                        id = doctor.id,
                        name = doctor.name,
                        lastname = doctor.lastname,
                        detail = specialityName
                    )
                } to beneficiaryName
            }.onSuccess { (pair, beneficiaryName) ->
                val (specialityName, professionals) = pair
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        subtitle = listOf(specialityName, beneficiaryName)
                            .filter { part -> part.isNotBlank() }
                            .joinToString(" · "),
                        professionals = professionals
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: ""
                    )
                }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}
