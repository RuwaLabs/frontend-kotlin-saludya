package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.TimeSlotRepository
import javax.inject.Inject

class GetTimeSlotsByDoctorIdAndDateUseCase @Inject constructor(
    private val repository: TimeSlotRepository
) {
    suspend operator fun invoke(doctorId: Int, date: String) =
        repository.getTimeSlotByDoctorIdAndDate(doctorId, date)
}
