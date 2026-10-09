package pe.edu.upc.saludya.features.patientreservations.presentation.sinCuposDia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetAvailableTimeSlotsBySpecialityIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SinCuposDiaViewModel @Inject constructor(
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getAvailableTimeSlots: GetAvailableTimeSlotsBySpecialityIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SinCuposDiaUiState())
    val uiState: StateFlow<SinCuposDiaUiState> = _uiState.asStateFlow()

    private var started = false

    fun start(specialityId: Int, date: String) {
        if (started) return
        started = true
        viewModelScope.launch {
            val specialityName = runCatching { getSpecialityById(specialityId)?.name }
                .getOrNull().orEmpty()
            _uiState.update {
                it.copy(specialityName = specialityName, date = date)
            }

            val next = findNextAvailability(specialityId, date)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    nextDate = next?.date,
                    nextStartHour = next?.startHour.orEmpty(),
                    nextEndHour = next?.endHour.orEmpty()
                )
            }
        }
    }

    private suspend fun findNextAvailability(
        specialityId: Int,
        fromDate: String
    ): NextAvailability? {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val base = runCatching { formatter.parse(fromDate) }.getOrNull() ?: return null
        val calendar = Calendar.getInstance().apply { time = base }
        for (offset in 1..14) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val date = formatter.format(calendar.time)
            val slots = runCatching {
                getAvailableTimeSlots(specialityId, date)
            }.getOrDefault(emptyList())
            val first = slots.firstOrNull() ?: continue
            return NextAvailability(
                date = date,
                startHour = first.startHour,
                endHour = first.endHour
            )
        }
        return null
    }

    private data class NextAvailability(
        val date: String,
        val startHour: String,
        val endHour: String
    )
}
