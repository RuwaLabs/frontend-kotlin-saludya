package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.icon.arrowBack

/**
 * Esqueleto común de todas las pantallas R01–R10 (375x812 en Figma):
 *  - Cabecera: x=24, y=34, volver 24dp + título 21sp, gap 12
 *  - Contenido: x=24, y=104 (40dp debajo de la cabecera), ancho 327, gap 14
 */
@Composable
fun SaludYaScreen(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SaludYaColors.Background)
    ) {
        Row(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 34.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = arrowBack,
                contentDescription = stringResource(R.string.reservation_back),
                tint = SaludYaColors.Primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBack)
            )
            Text(
                text = title,
                style = saludYaText(21, FontWeight.SemiBold)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content
        )
    }
}
