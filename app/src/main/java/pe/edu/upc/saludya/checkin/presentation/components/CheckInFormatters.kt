package pe.edu.upc.saludya.checkin.presentation.components

import pe.edu.upc.saludya.checkin.domain.entity.Reservation
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/*
 * Formatos de presentación. Viven fuera del dominio para no mezclarlo con textos de UI.
 */

private val EsPe: Locale = Locale.forLanguageTag("es-PE")
private val DayMonth = DateTimeFormatter.ofPattern("d MMM", EsPe)
private val WeekDay = DateTimeFormatter.ofPattern("EEE", EsPe)
private val Hour = DateTimeFormatter.ofPattern("HH:mm")

/** Algunas versiones de CLDR devuelven "oct." / "mar." con punto; se normaliza. */
private fun String.withoutDots(): String = replace(".", "")

/** "09:00" */
fun LocalTime.toHourLabel(): String = format(Hour)

/** "08:55" */
fun LocalDateTime.toHourLabel(): String = format(Hour)

/** "Medicina general · 6 oct · 09:00" */
fun Reservation.toDisplayLabel(): String =
    "$specialty · ${date.format(DayMonth).withoutDots()} · ${startHour.toHourLabel()}"

/** "Mar 6 oct · 09:00–09:30" */
fun Reservation.toScheduleLabel(): String {
    val weekDay = date.format(WeekDay).withoutDots().replaceFirstChar { it.titlecase(EsPe) }
    return "$weekDay ${date.format(DayMonth).withoutDots()} · ${startHour.toHourLabel()}–${endHour.toHourLabel()}"
}

/**
 * "Dr(a). Ana Ríos". La tabla `doctors` no guarda género ni tratamiento, por lo que
 * no es posible distinguir "Dr." de "Dra.".
 */
fun Reservation.toDoctorLabel(): String = "Dr(a). $doctorFullName"

/** "Consultorio 02 · Sala A", o `null` si el esquema no aporta ubicación. */
fun Reservation.toLocationLabel(): String? =
    listOfNotNull(
        consultingRoom?.let { "Consultorio $it" },
        waitingRoom?.let { "Sala $it" },
    ).joinToString(" · ").ifEmpty { null }

/** "Sala de espera A · Consultorio 02", o `null` si el esquema no aporta ubicación. */
fun Reservation.toWaitingLocationLabel(): String? =
    listOfNotNull(
        waitingRoom?.let { "Sala de espera $it" },
        consultingRoom?.let { "Consultorio $it" },
    ).joinToString(" · ").ifEmpty { null }
