package pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper

import pe.edu.upc.saludya.features.patientreservations.domain.model.Doctor
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorDto

fun DoctorDto.toDomain() = Doctor(
    id = id,
    specialityId = specialtyId,
    name = name,
    lastname = lastname
)
