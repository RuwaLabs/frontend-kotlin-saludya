package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import javax.inject.Inject

class GetAppointmentByIdUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: Int) = repository.getAppointmentById(id)
}
