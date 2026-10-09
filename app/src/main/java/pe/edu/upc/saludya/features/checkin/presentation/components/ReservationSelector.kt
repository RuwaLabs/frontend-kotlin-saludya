package pe.edu.upc.saludya.features.checkin.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation

/**
 * Selector desplegable de reserva: etiqueta + Card interactiva con borde tenue que abre
 * un [DropdownMenu] con las reservas disponibles ("Medicina general · 6 oct · 09:00").
 *
 * @param reservations reservas disponibles para elegir.
 * @param selected reserva seleccionada (o `null` si no hay ninguna).
 * @param onReservationSelected callback con la reserva elegida.
 * @param label etiqueta mostrada encima del selector.
 */
@Composable
fun ReservationSelector(
    reservations: List<Reservation>,
    selected: Reservation?,
    onReservationSelected: (Reservation) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Selecciona una reserva",
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = colors.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Box {
            Card(
                onClick = { expanded = true },
                enabled = reservations.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface,
                    disabledContainerColor = colors.surface,
                ),
                border = BorderStroke(1.dp, colors.onSurfaceVariant.copy(alpha = 0.2f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = selected?.toDisplayLabel() ?: "Sin reservas disponibles",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "▾",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                reservations.forEach { reservation ->
                    val isSelected = reservation.id == selected?.id
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = reservation.toDisplayLabel(),
                                color = if (isSelected) colors.primary else colors.onSurface,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            )
                        },
                        onClick = {
                            expanded = false
                            onReservationSelected(reservation)
                        },
                    )
                }
            }
        }
    }
}
