package pe.edu.upc.saludya.reassignment.presentation.offerdetail

import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class OfferDetailUiModel(
    val title: String = "Reasignación disponible",
    val currentSchedule: InfoCardUiModel = InfoCardUiModel(
        title = "Horario actual",
        lines = listOf("6 oct · 09:00–09:30", "Medicina general · Dra. Ana Ríos"),
    ),
    val offeredSchedule: InfoCardUiModel = InfoCardUiModel(
        title = "Nuevo horario ofrecido",
        lines = listOf("6 oct · 08:30–09:00", "Medicina general · Dra. Ana Ríos"),
        footer = "Responde antes de las 08:05",
    ),
    val note: String = "Si aceptas, la nueva reserva reemplazará tu horario actual.",
    val acceptText: String = "Aceptar nuevo horario",
    val rejectText: String = "Rechazar oferta",
)
