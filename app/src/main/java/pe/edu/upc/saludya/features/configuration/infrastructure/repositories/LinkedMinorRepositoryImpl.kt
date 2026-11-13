package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import com.google.gson.Gson
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.ErrorDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.LinkMinorRequest
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientMinorDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject

class LinkedMinorRepositoryImpl @Inject constructor(
    private val patients: PatientService,
    private val session: SessionProvider,
) : LinkedMinorRepository {

    private val gson = Gson()

    override suspend fun getLinkedMinors(): List<LinkedMinor> {
        val patientId = session.patientId ?: error("Inicia sesión para consultar tus menores")
        val response = patients.getMinors(patientId)
        if (!response.isSuccessful) error("No se pudieron cargar tus menores: ${response.code()}")
        val tutor = patients.getPatient(patientId).body()
            ?: error("No se pudo consultar al titular")
        return response.body().orEmpty().map { toLinkedMinor(it, "${tutor.name} ${tutor.lastname}") }
    }

    override suspend fun getLinkedMinorById(id: String): LinkedMinor? =
        getLinkedMinors().firstOrNull { it.id == id }

    override suspend fun unlink(id: String) {
        val response = patients.unlinkMinor(id.toInt())
        if (!response.isSuccessful) error("No se pudo desvincular al menor: ${response.code()}")
    }

    override suspend fun linkMinor(
        dni: String,
        name: String,
        lastname: String,
        birthDate: String,
    ): String {
        val response = patients.linkMinor(
            LinkMinorRequest(
                dni = dni,
                name = name,
                lastname = lastname,
                birthDate = birthDate,
            )
        )
        val link = response.body()
        if (response.isSuccessful && link != null) return link.id.toString()

        val code = response.errorBody()?.string()?.let {
            runCatching { gson.fromJson(it, ErrorDto::class.java)?.code }.getOrNull()
        }
        error(
            when (code) {
                "IAM_MINOR_ALREADY_LINKED" -> "Este menor ya está vinculado."
                "IAM_BENEFICIARY_HAS_ACCOUNT" -> "Esta identidad ya tiene una cuenta independiente."
                "IAM_MINOR_REQUIRED" -> "El beneficiario debe ser menor de 18 años."
                "IAM_ADULT_REQUIRED" -> "El tutor debe ser mayor de edad."
                else -> "No se pudo vincular al menor. Verifica los datos."
            }
        )
    }

    private suspend fun toLinkedMinor(link: PatientMinorDto, tutorName: String): LinkedMinor {
        val minor = patients.getPatient(link.patientId).body()
            ?: error("No se pudo consultar el menor")
        val date = minor.birthDate ?: error("No se encontró la fecha de nacimiento")
        return LinkedMinor(
            id = link.id.toString(),
            fullName = "${minor.name} ${minor.lastname}",
            age = Period.between(LocalDate.parse(date), LocalDate.now()).years,
            documentType = "DNI",
            documentNumberMasked = "••••${minor.dni.takeLast(4)}",
            birthDate = date,
            representativeName = tutorName,
            isVerified = true,
        )
    }
}
