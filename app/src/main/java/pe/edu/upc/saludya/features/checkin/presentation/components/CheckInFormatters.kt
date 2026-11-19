package pe.edu.upc.saludya.features.checkin.presentation.components

import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.content.Context
import pe.edu.upc.saludya.R

/*
 * Formatos de presentación. Viven fuera del dominio para no mezclarlo con textos de UI.
 */

private val EsPe: Locale = Locale.forLanguageTag("es-PE")
private val DayMonth = DateTimeFormatter.ofPattern("d MMM", EsPe)
private val WeekDay = DateTimeFormatter.ofPattern("EEE", EsPe)
private val Hour = DateTimeFormatter.ofPattern("HH:mm")

/** Algunas versiones de CLDR devuelven "oct." / "mar." con punto; se normaliza. */
private fun String.withoutDots(): String = replace(".", "")

private fun String.asConsultingRoom(context: Context): String =
    if (startsWith("Consultorio", ignoreCase = true)) {
        this
    } else {
        context.getString(R.string.checkin_consulting_room, this)
    }

/** "09:00" */
fun LocalTime.toHourLabel(): String = format(Hour)

/** "08:55" */
fun LocalDateTime.toHourLabel(): String = format(Hour)

/** "Medicina general · 6 oct · 09:00" */
fun Reservation.toDisplayLabel(context: Context): String =
    context.getString(
        R.string.checkin_reservation_label,
        specialty,
        date.format(DayMonth).withoutDots(),
        startHour.toHourLabel(),
    )

/** "Mar 6 oct · 09:00–09:30" */
fun Reservation.toScheduleLabel(context: Context): String {
    val weekDay = date.format(WeekDay)
        .withoutDots()
        .replaceFirstChar { it.titlecase(EsPe) }

    return context.getString(
        R.string.checkin_reservation_schedule,
        weekDay,
        date.format(DayMonth).withoutDots(),
        startHour.toHourLabel(),
        endHour.toHourLabel(),
    )
}

/**
 * "Dr(a). Ana Ríos". La tabla `doctors` no guarda género ni tratamiento, por lo que
 * no es posible distinguir "Dr." de "Dra.".
 */
fun Reservation.toDoctorLabel(context: Context): String =
    context.getString(R.string.checkin_doctor_name, doctorFullName)

/** "Consultorio 02 · Sala A", o `null` si el esquema no aporta ubicación. */
fun Reservation.toLocationLabel(context: Context): String? =
    listOfNotNull(
        consultingRoom?.asConsultingRoom(context),
        waitingRoom?.let {
            context.getString(R.string.checkin_room, it)
        },
    ).joinToString(" · ").ifEmpty { null }

/** "Sala de espera A · Consultorio 02", o `null` si el esquema no aporta ubicación. */
fun Reservation.toWaitingLocationLabel(context: Context): String? =
    listOfNotNull(
        waitingRoom?.let {
            context.getString(R.string.checkin_waiting_room, it)
        },
        consultingRoom?.asConsultingRoom(context),
    ).joinToString(" · ").ifEmpty { null }