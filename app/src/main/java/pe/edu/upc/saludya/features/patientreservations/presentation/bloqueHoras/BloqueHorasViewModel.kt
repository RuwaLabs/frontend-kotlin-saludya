package pe.edu.upc.saludya.features.patientreservations.presentation.bloqueHoras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetAvailableTimeSlotsBySpecialityIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetDoctorsBySpecialityIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetTimeSlotsByDoctorIdAndDateUseCase
import pe.edu.upc.saludya.features.patientreservations.domain.model.Doctor
import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeSlot
import pe.edu.upc.saludya.features.patientreservations.presentation.util.toHhMm
import javax.inject.Inject

@HiltViewModel
class BloqueHorasViewModel @Inject constructor(
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getDoctorsBySpecialityId: GetDoctorsBySpecialityIdUseCase,
    private val getTimeSlotsByDoctorIdAndDate: GetTimeSlotsByDoctorIdAndDateUseCase,
    private val getAvailableTimeSlots: GetAvailableTimeSlotsBySpecialityIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BloqueHorasUiState(isLoading = true))
    val uiState: StateFlow<BloqueHorasUiState> = _uiState.asStateFlow()

    private var started = false

    fun start(specialityId: Int, beneficiaryId: Int, date: String, doctorId: Int?) {
        if (started) return
        started = true
        viewModelScope.launch {
            runCatching {
                val specialityName = getSpecialityById(specialityId)?.name.orEmpty()
                val doctorsById = getDoctorsBySpecialityId(specialityId).associateBy { it.id }
                val slots = if (doctorId != null) {
                    getTimeSlotsByDoctorIdAndDate(doctorId, date)
                } else {
                    getAvailableTimeSlots(specialityId, date)
                }
                val doctor = doctorId?.let { doctorsById[it] }
                val firstRoom = slots.firstOrNull()?.room.orEmpty()
                Triple(specialityName, doctor, Triple(doctorsById, slots, firstRoom))
            }.onSuccess { (specialityName, doctor, payload) ->
                val (doctorsById, slots, firstRoom) = payload
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        isProfessional = doctor != null,
                        doctorName = doctor?.name.orEmpty(),
                        doctorLastname = doctor?.lastname.orEmpty(),
                        room = firstRoom,
                        date = date,
                        specialityName = specialityName,
                        hasSlots = slots.isNotEmpty(),
                        slots = slots.map { slot -> slot.toUi(doctorsById) }
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

    fun onSlotClick(slotId: Int) {
        _uiState.update { state ->
            val slot = state.slots.firstOrNull { it.id == slotId }
            if (slot == null || slot.availableSpots <= 0) return@update state
            state.copy(
                selectedSlotId = slotId,
                slots = state.slots.map { it.copy(selected = it.id == slotId) }
            )
        }
    }
}

private fun TimeSlot.toUi(doctorsById: Map<Int, Doctor>): BloqueHoraUi {
    val doctor = doctorsById[doctorId]
    return BloqueHoraUi(
        id = id,
        range = "${startHour.toHhMm()}–${endHour.toHhMm()}",
        doctorName = doctor?.name.orEmpty(),
        doctorLastname = doctor?.lastname.orEmpty(),
        availableSpots = maxCapacity - currentBookings,
        selected = false
    )
}
