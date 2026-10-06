package pe.edu.upc.saludya.features.patientreservations.domain.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Doctor

interface DoctorRepository {
    suspend fun getDoctorById(id: Int): Doctor?
    suspend fun getDoctorsBySpecialityId(specialityId: Int): List<Doctor>
}
