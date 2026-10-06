package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.domain.repository.PatientRepository
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor() : PatientRepository {

    override suspend fun getPatientProfile(): Patient = Patient(
        id = "patient_001",
        fullName = "Lucía Torres",
        role = "Titular",
        documentType = "DNI",
        documentNumberMasked = "••••4821",
    )
}