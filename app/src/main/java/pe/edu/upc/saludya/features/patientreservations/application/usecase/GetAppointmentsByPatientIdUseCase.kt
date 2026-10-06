package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import javax.inject.Inject

class GetAppointmentsByPatientIdUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(patientId: Int) = repository.getAppointmentsByPatientId(patientId)
}
