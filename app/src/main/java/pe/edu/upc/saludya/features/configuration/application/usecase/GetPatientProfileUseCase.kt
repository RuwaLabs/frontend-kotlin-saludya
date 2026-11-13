package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.domain.repository.PatientRepository
import javax.inject.Inject

class GetPatientProfileUseCase @Inject constructor(
    private val repository: PatientRepository,
) {
    suspend operator fun invoke(): Patient = repository.getPatientProfile()
}