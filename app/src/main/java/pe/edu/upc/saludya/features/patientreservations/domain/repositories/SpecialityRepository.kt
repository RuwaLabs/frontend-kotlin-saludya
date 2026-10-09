package pe.edu.upc.saludya.features.patientreservations.domain.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Speciality

interface SpecialityRepository {
    suspend fun getSpecialityById(id: Int): Speciality?
    suspend fun getAllSpecialities(): List<Speciality>
}
