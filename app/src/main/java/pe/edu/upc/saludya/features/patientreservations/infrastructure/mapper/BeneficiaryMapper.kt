package pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper

import pe.edu.upc.saludya.features.patientreservations.domain.model.Beneficiary
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientDto

fun PatientDto.toDomain(titular: Boolean) = Beneficiary(
    patientId = id,
    name = name,
    lastname = lastname,
    dni = dni,
    titular = titular
)
