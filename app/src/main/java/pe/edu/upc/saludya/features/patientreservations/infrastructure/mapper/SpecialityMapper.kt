package pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper

import pe.edu.upc.saludya.features.patientreservations.domain.model.Speciality
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityDto

fun SpecialityDto.toDomain() = Speciality(
    id = id,
    name = name,
    description = description
)
