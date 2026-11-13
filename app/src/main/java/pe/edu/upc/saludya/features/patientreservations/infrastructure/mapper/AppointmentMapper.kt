package pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper

import pe.edu.upc.saludya.features.patientreservations.domain.model.Appointment
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentDto

fun AppointmentDto.toDomain() = Appointment(
    id = id,
    timeSlotId = timeSlotId,
    patientId = patientId,
    bookingOrder = bookingOrder,
    bookingCode = bookingCode,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)
