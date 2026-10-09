package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Speciality
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.SpecialityRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper.toDomain
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityService
import javax.inject.Inject

class SpecialityRepositoryImpl @Inject constructor(
    private val service: SpecialityService
) : SpecialityRepository {

    override suspend fun getSpecialityById(id: Int): Speciality? {
        try {
            val response = service.getSpecialityById(id)
            if (response.isSuccessful) {
                response.body()?.let { return it.toDomain() }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getAllSpecialities(): List<Speciality> {
        try {
            val response = service.getAllSpecialities()
            if (response.isSuccessful) {
                return response.body().orEmpty().map { it.toDomain() }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }
}
