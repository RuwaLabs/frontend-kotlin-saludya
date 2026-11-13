package pe.edu.upc.saludya.features.configuration.domain.repository

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient

interface PatientRepository {
    suspend fun getPatientProfile(): Patient
}