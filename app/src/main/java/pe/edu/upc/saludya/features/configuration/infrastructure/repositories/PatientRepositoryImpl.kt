package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.domain.repository.PatientRepository
import pe.edu.upc.saludya.features.iam.infrastructure.remote.AccessApi
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val session: SessionProvider,
    private val accounts: AccessApi,
    private val patients: PatientService,
) : PatientRepository {

    override suspend fun getPatientProfile(): Patient {
        val patientId = session.patientId ?: error("Inicia sesión para consultar tu perfil")
        val token = session.accessToken ?: error("Inicia sesión para consultar tu perfil")
        val account = accounts.profile("Bearer $token").body()
            ?: error("No se pudo consultar tu cuenta")
        if (account.role != "PATIENT" || account.patientId != patientId.toLong()) {
            error("La cuenta no corresponde al paciente")
        }
        val profile = patients.getPatient(patientId).body()
            ?: error("No se pudo consultar el perfil")
        return Patient(
            id = profile.id.toString(),
            fullName = "${profile.name} ${profile.lastname}",
            role = "Titular",
            documentType = "DNI",
            documentNumberMasked = "••••${profile.dni.takeLast(4)}",
            phoneNumber = profile.phone.orEmpty(),
            email = account.email.orEmpty(),
        )
    }
}
