package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.patientreservations.presentation.util.doctorDisplayName
import pe.edu.upc.saludya.features.patientreservations.presentation.util.shortDateLabel
import pe.edu.upc.saludya.features.patientreservations.presentation.util.toHhMm

/**
 * Datos crudos de una reserva. El texto para el usuario se arma en la UI
 * con stringResource (ver [SaludYaReservaSummaryCard] y [toDialogLines]).
 */
data class ReservaSummaryUi(
    val specialityName: String,
    val beneficiaryName: String,
    val beneficiaryTitular: Boolean,
    val doctorName: String,
    val doctorLastname: String,
    val date: String,
    val startHour: String,
    val endHour: String,
    val room: String
)

@Composable
private fun ReservaSummaryUi.beneficiaryLine(): String {
    val role = stringResource(
        if (beneficiaryTitular) {
            R.string.reservation_beneficiary_titular_label
        } else {
            R.string.reservation_beneficiary_minor_label
        }
    )
    return stringResource(R.string.reservation_beneficiary_line, beneficiaryName, role)
}

@Composable
private fun ReservaSummaryUi.doctorLine(): String = if (doctorName.isBlank()) {
    stringResource(R.string.reservation_professional_default)
} else {
    doctorDisplayName(doctorName, doctorLastname)
}

@Composable
private fun ReservaSummaryUi.dateTimeLine(): String = stringResource(
    R.string.reservation_datetime,
    shortDateLabel(date),
    startHour.toHhMm(),
    endHour.toHhMm()
)

@Composable
private fun ReservaSummaryUi.placeLine(): String =
    stringResource(R.string.reservation_place, room)

@Composable
fun SaludYaReservaSummaryCard(
    summary: ReservaSummaryUi,
    status: String,
    modifier: Modifier = Modifier
) {
    SaludYaInfoCard(
        modifier = modifier,
        title = summary.specialityName,
        body = listOf(
            summary.beneficiaryLine(),
            summary.doctorLine(),
            summary.dateTimeLine(),
            summary.placeLine()
        ),
        status = status
    )
}

/** Líneas del resumen para los diálogos de confirmación/cancelación. */
@Composable
fun ReservaSummaryUi.toDialogLines(): List<String> = listOf(
    beneficiaryLine(),
    "${specialityName} · ${doctorLine()}",
    dateTimeLine(),
    placeLine()
)
