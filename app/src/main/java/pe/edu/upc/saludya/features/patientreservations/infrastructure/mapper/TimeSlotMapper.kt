package pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper

import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeSlot
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotDto

fun TimeSlotDto.toDomain() = TimeSlot(
    id = id,
    doctorId = doctorId,
    date = date,
    startHour = startHour,
    endHour = endHour,
    room = room,
    maxCapacity = maxCapacity,
    currentBookings = currentBookings,
    status = status
)
