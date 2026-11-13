package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.DoctorRepository
import javax.inject.Inject

class GetDoctorsBySpecialityIdUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    suspend operator fun invoke(specialityId: Int) = repository.getDoctorsBySpecialityId(specialityId)
}
