package pe.edu.upc.saludya.features.patientreservations.presentation.elegirFecha

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetAvailableTimeSlotsBySpecialityIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetBeneficiaryByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ElegirFechaViewModel @Inject constructor(
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getAvailableTimeSlots: GetAvailableTimeSlotsBySpecialityIdUseCase,
    private val getBeneficiaryById: GetBeneficiaryByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ElegirFechaUiState())
    val uiState: StateFlow<ElegirFechaUiState> = _uiState.asStateFlow()

    private var specialityId: Int = 0
    private val currentMonth: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }
    private var started = false

    fun start(specialityId: Int, beneficiaryId: Int) {
        if (started) return
        started = true
        this.specialityId = specialityId
        viewModelScope.launch {
            val name = runCatching { getSpecialityById(specialityId)?.name }.getOrNull().orEmpty()
            val beneficiary = runCatching { getBeneficiaryById(beneficiaryId) }.getOrNull()
            _uiState.update {
                it.copy(specialityName = name, beneficiaryName = beneficiary?.displayName.orEmpty())
            }
        }
        loadMonth()
    }

    fun onPreviousMonth() {
        currentMonth.add(Calendar.MONTH, -1)
        loadMonth()
    }

    fun onNextMonth() {
        currentMonth.add(Calendar.MONTH, 1)
        loadMonth()
    }

    fun onDayClick(day: Int) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    private fun loadMonth() {
        val year = currentMonth.get(Calendar.YEAR)
        val monthValue = currentMonth.get(Calendar.MONTH) + 1
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOffset = (currentMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    year = year,
                    month = monthValue,
                    firstDayOffset = firstDayOffset,
                    daysInMonth = daysInMonth,
                    enabledDays = emptySet(),
                    selectedDay = null
                )
            }
            runCatching { computeEnabledDays(year, monthValue, daysInMonth) }
                .onSuccess { days ->
                    _uiState.update { it.copy(isLoading = false, enabledDays = days) }
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

    private suspend fun computeEnabledDays(
        year: Int,
        monthValue: Int,
        daysInMonth: Int
    ): Set<Int> = coroutineScope {
        (1..daysInMonth).map { day ->
            async {
                val date = String.format("%04d-%02d-%02d", year, monthValue, day)
                val slots = runCatching {
                    getAvailableTimeSlots(specialityId, date)
                }.getOrDefault(emptyList())
                if (slots.isNotEmpty()) day else null
            }
        }.awaitAll().filterNotNull().toSet()
    }
}
