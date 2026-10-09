package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.DoctorRepository
import javax.inject.Inject

class GetDoctorByIdUseCase @Inject constructor(
    private val repository: DoctorRepository
) {
    suspend operator fun invoke(id: Int) = repository.getDoctorById(id)
}
