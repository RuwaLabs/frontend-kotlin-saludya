package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.TimeSlotRepository
import javax.inject.Inject

class GetTimeSlotByIdUseCase @Inject constructor(
    private val repository: TimeSlotRepository
) {
    suspend operator fun invoke(id: Int) = repository.getTimeSlotById(id)
}
