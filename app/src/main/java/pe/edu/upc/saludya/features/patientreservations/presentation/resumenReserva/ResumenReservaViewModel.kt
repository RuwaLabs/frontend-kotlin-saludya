package pe.edu.upc.saludya.features.patientreservations.presentation.resumenReserva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.features.patientreservations.application.usecase.BookAppointmentUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetBeneficiaryByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetDoctorByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetSpecialityByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.GetTimeSlotByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.domain.model.AppointmentConflictException
import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeslotConflictException
import pe.edu.upc.saludya.features.patientreservations.presentation.components.ReservaSummaryUi
import javax.inject.Inject

@HiltViewModel
class ResumenReservaViewModel @Inject constructor(
    private val getTimeSlotById: GetTimeSlotByIdUseCase,
    private val getDoctorById: GetDoctorByIdUseCase,
    private val getSpecialityById: GetSpecialityByIdUseCase,
    private val getBeneficiaryById: GetBeneficiaryByIdUseCase,
    private val bookAppointment: BookAppointmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResumenReservaUiState())
    val uiState: StateFlow<ResumenReservaUiState> = _uiState.asStateFlow()

    private var beneficiaryId: Int = 0
    private var timeSlotId: Int = 0
    private var started = false

    fun start(specialityId: Int, beneficiaryId: Int, timeSlotId: Int) {
        if (started) return

        started = true
        this.beneficiaryId = beneficiaryId
        this.timeSlotId = timeSlotId

        viewModelScope.launch {
            try {
                val slot = getTimeSlotById(timeSlotId)
                    ?: throw IllegalStateException("No se encontró el horario seleccionado.")

                val doctor = getDoctorById(slot.doctorId)
                val specialityName = getSpecialityById(specialityId)?.name.orEmpty()

                val beneficiary = try {
                    getBeneficiaryById(beneficiaryId)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }

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

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        summary = summary,
                        beneficiaryName = beneficiary?.displayName.orEmpty()
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                            ?: "No se pudo cargar el resumen de la reserva."
                    )
                }
            }
        }
    }

    fun onConfirmClick() {
        _uiState.update { it.copy(dialog = ReservaDialog.CONFIRMAR) }
    }

    fun onDismissDialog() {
        _uiState.update { it.copy(dialog = ReservaDialog.NONE) }
    }

    fun onConfirmBooking() {
        if (_uiState.value.isBooking || _uiState.value.bookedAppointmentId != null) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isBooking = true, dialog = ReservaDialog.NONE)
            }

            bookAppointment(
                patientId = beneficiaryId,
                timeSlotId = timeSlotId
            ).onSuccess { appointment ->
                _uiState.update {
                    it.copy(
                        isBooking = false,
                        bookedAppointmentId = appointment.id,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                if (error is CancellationException) throw error

                val isConflict = error is TimeslotConflictException ||
                        error is AppointmentConflictException

                _uiState.update {
                    it.copy(
                        isBooking = false,
                        errorMessage = if (isConflict) null else {
                            error.message ?: "No se pudo completar la reserva. Intenta nuevamente."
                        },
                        dialog = when (error) {
                            is TimeslotConflictException -> ReservaDialog.CUPO_OCUPADO
                            is AppointmentConflictException -> ReservaDialog.CRUCE
                            else -> ReservaDialog.NONE
                        }
                    )
                }
            }
        }
    }
}
