package pe.edu.upc.saludya.features.iam.domain.repository

/** Guarda la sesión del paciente en el dispositivo. */
interface PatientSessionStorage {
    suspend fun save(session: PatientSession)
    suspend fun read(): PatientSession?
    suspend fun clear()
}
