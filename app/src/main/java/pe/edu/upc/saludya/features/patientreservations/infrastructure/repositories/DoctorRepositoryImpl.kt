package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Doctor
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.DoctorRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val service: DoctorService
) : DoctorRepository {

    override suspend fun getDoctorById(id: Int): Doctor? {
        try {
            val response = service.getDoctorById(id)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    return Doctor(
                        id = dto.id,
                        specialityId = dto.specialtyId,
                        name = dto.name,
                        lastname = dto.lastname
                    )
                }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getDoctorsBySpecialityId(specialityId: Int): List<Doctor> {
        try {
            val response = service.getDoctorsBySpecialityId(specialityId)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        Doctor(
                            id = dto.id,
                            specialityId = dto.specialtyId,
                            name = dto.name,
                            lastname = dto.lastname
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }
}
