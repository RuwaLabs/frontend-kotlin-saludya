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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.Locale

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
    private var loadMonthJob: Job? = null

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
        val todayMonth = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }

        if (!currentMonth.after(todayMonth)) return

        currentMonth.add(Calendar.MONTH, -1)
        loadMonth()
    }

    fun onNextMonth() {
        currentMonth.add(Calendar.MONTH, 1)
        loadMonth()
    }

    fun onDayClick(day: Int) {
        val state = _uiState.value

        if (isPastDate(state.year, state.month, day)) return

        _uiState.update { it.copy(selectedDay = day) }
    }

    private fun loadMonth() {
        val year = currentMonth.get(Calendar.YEAR)
        val monthValue = currentMonth.get(Calendar.MONTH) + 1
        val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOffset = (currentMonth.get(Calendar.DAY_OF_WEEK) + 5) % 7

        loadMonthJob?.cancel()

        loadMonthJob = viewModelScope.launch {
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

            try {
                val days = computeEnabledDays(year, monthValue, daysInMonth)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        enabledDays = days
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                            ?: "No se pudo consultar la disponibilidad."
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
        val semaphore = Semaphore(4)

        (1..daysInMonth).map { day ->
            async {
                if (isPastDate(year, monthValue, day)) {
                    return@async null
                }

                semaphore.withPermit {
                    val date = String.format(
                        Locale.ROOT,
                        "%04d-%02d-%02d",
                        year,
                        monthValue,
                        day
                    )

                    val slots = getAvailableTimeSlots(specialityId, date)

                    if (slots.any { it.maxCapacity > it.currentBookings }) {
                        day
                    } else {
                        null
                    }
                }
            }
        }.awaitAll().filterNotNull().toSet()
    }

    private fun isPastDate(
        year: Int,
        monthValue: Int,
        day: Int
    ): Boolean {
        val selectedDate = Calendar.getInstance().apply {
            set(year, monthValue - 1, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return selectedDate.before(today)
    }
}
