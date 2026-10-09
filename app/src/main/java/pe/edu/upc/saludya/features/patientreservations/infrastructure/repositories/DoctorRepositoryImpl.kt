package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Doctor
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.DoctorRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper.toDomain
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val service: DoctorService
) : DoctorRepository {

    override suspend fun getDoctorById(id: Int): Doctor? {
        try {
            val response = service.getDoctorById(id)
            if (response.isSuccessful) {
                response.body()?.let { return it.toDomain() }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getDoctorsBySpecialityId(specialityId: Int): List<Doctor> {
        try {
            val response = service.getDoctorsBySpecialityId(specialityId)
            if (response.isSuccessful) {
                return response.body().orEmpty().map { it.toDomain() }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }
}
