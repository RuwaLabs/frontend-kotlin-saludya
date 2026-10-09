package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import javax.inject.Inject

class GetAppointmentsByTimeSlotIdUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(timeSlotId: Int) = repository.getAppointmentsByTimeSlotId(timeSlotId)
}
