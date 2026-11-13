package pe.edu.upc.saludya.features.patientreservations.presentation.reservaConfirmada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetAppointmentByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetBeneficiaryByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetDoctorByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetTimeSlotByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.presentation.components.ReservaSummaryUi
import javax.inject.Inject

@HiltViewModel
class ReservaConfirmadaViewModel @Inject constructor(
    private val getAppointmentById: GetAppointmentByIdUseCase,
    private val getTimeSlotById: GetTimeSlotByIdUseCase,
    private val getDoctorById: GetDoctorByIdUseCase,
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getBeneficiaryById: GetBeneficiaryByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservaConfirmadaUiState())
    val uiState: StateFlow<ReservaConfirmadaUiState> = _uiState.asStateFlow()

    private var started = false

    fun start(appointmentId: Int) {
        if (started) return
        started = true
        viewModelScope.launch {
            runCatching {
                val appointment = getAppointmentById(appointmentId) ?: error("")
                val slot = getTimeSlotById(appointment.timeSlotId) ?: error("")
                val doctor = getDoctorById(slot.doctorId)
                val specialityName = doctor
                    ?.let { getSpecialityById(it.specialityId)?.name }
                    .orEmpty()
                val beneficiary = runCatching { getBeneficiaryById(appointment.patientId) }.getOrNull()
                val summary = ReservaSummaryUi(
                    specialityName = specialityName,
                    beneficiaryName = beneficiary?.displayName.orEmpty(),
                    beneficiaryTitular = beneficiary?.titular == true,
                    doctorName = doctor?.name.orEmpty(),
                    doctorLastname = doctor?.lastname.orEmpty(),
                    date = slot.date,
                    startHour = slot.startHour,
                    endHour = slot.endHour,
                    room = slot.room
                )
                summary to appointment.bookingCode
            }.onSuccess { (summary, bookingCode) ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = null, summary = summary, bookingCode = bookingCode)
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
}
