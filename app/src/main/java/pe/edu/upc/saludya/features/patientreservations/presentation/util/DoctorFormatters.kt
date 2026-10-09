package pe.edu.upc.saludya.features.patientreservations.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** "Dra. Lucía Torres" / "Dr. Carlos Vega" según el nombre. */
@Composable
fun doctorDisplayName(name: String, lastname: String): String {
    val treatment = if (name.trim().endsWith("a", ignoreCase = true)) {
        stringResource(R.string.reservation_doctor_prefix_female)
    } else {
        stringResource(R.string.reservation_doctor_prefix_male)
    }
    return "$treatment $name $lastname"
}
