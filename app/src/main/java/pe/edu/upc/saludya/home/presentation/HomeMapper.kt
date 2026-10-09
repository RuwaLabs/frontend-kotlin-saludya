package pe.edu.upc.saludya.home.presentation

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel
import pe.edu.upc.saludya.home.domain.HomeAppointment

fun HomeAppointment.toAppointmentUiModel(): AppointmentUiModel {
    val whenText = listOf(date, time).filter { it.isNotBlank() }.joinToString(" · ")
    val details = buildList {
        if (patientName.isNotBlank()) add(patientName)
        if (doctorName.isNotBlank()) add(doctorName)
        if (whenText.isNotBlank()) add(whenText)
        if (place.isNotBlank()) add(place)
    }
    return AppointmentUiModel(
        specialty = specialty,
        details = details,
        status = status
    )
}
