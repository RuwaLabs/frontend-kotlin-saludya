package pe.edu.upc.saludya.features.patientreservations.presentation.bloqueHoras

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.patientreservations.presentation.util.doctorDisplayName

data class BloqueHoraUi(
    val id: Int,
    val range: String,
    val doctorName: String,
    val doctorLastname: String,
    val availableSpots: Int,
    val selected: Boolean
) {
    @Composable
    fun doctorDisplay(): String = if (doctorName.isBlank()) {
        stringResource(R.string.reservation_professional_default)
    } else {
        doctorDisplayName(doctorName, doctorLastname)
    }

    @Composable
    fun statusText(): String = when {
        availableSpots <= 0 -> stringResource(R.string.reservation_slot_no_spots)
        selected && availableSpots == 1 -> stringResource(R.string.reservation_slot_selected_one)
        selected -> stringResource(R.string.reservation_slot_selected_many, availableSpots)
        availableSpots == 1 -> stringResource(R.string.reservation_slot_available_one)
        else -> stringResource(R.string.reservation_slot_available_many, availableSpots)
    }
}

data class BloqueHorasUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isProfessional: Boolean = false,
    val doctorName: String = "",
    val doctorLastname: String = "",
    val room: String = "",
    val date: String = "",
    val specialityName: String = "",
    val slots: List<BloqueHoraUi> = emptyList(),
    val selectedSlotId: Int? = null,
    val hasSlots: Boolean = true
)
