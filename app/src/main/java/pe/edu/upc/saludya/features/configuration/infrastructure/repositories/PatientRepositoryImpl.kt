package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.domain.repository.PatientRepository
import pe.edu.upc.saludya.features.configuration.infrastructure.remote.PatientApi
import pe.edu.upc.saludya.iam.domain.model.PatientSessionStore
import java.io.IOException
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val api: PatientApi,
    private val sessions: PatientSessionStore,
) : PatientRepository {

    override suspend fun getPatientProfile(): Patient {
        val session = sessions.current()
            ?: throw IOException("No active session")

        val response = api.getMyAccount(
            authorization = "Bearer ${session.accessToken}",
        )

        if (response.code() == 401) {
            sessions.expireIfToken(session.accessToken)
            throw IOException("Session expired")
        }

        if (!response.isSuccessful) {
            throw IOException("Unable to retrieve account")
        }

        val account = response.body()
            ?: throw IOException("Empty account response")

        if (
            account.id != session.userId ||
            account.patientId != session.patientId ||
            account.role != "PATIENT" ||
            account.active != true
        ) {
            throw IOException("Invalid patient account")
        }

        val dni = account.dni
            ?.takeIf { it.matches(Regex("[0-9]{8}")) }
            ?: throw IOException("Invalid patient DNI")

        val fullName = listOfNotNull(
            account.name?.takeIf { it.isNotBlank() },
            account.lastname?.takeIf { it.isNotBlank() },
        ).joinToString(" ")

        if (fullName.isBlank()) {
            throw IOException("Invalid patient name")
        }

        val email = account.email
            ?.takeIf { it.isNotBlank() }
            ?: throw IOException("Invalid patient email")

        return Patient(
            id = account.patientId.toString(),
            fullName = fullName,
            role = "PATIENT",
            documentType = "DNI",
            documentNumberMasked = "\u2022\u2022\u2022\u2022${dni.takeLast(4)}",
            phoneNumber = account.phone.orEmpty(),
            email = email,
        )
    }
}