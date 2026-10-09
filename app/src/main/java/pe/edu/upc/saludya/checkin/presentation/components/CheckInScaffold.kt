package pe.edu.upc.saludya.checkin.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar

/** Índice de la pestaña "Asistencia" en la barra inferior. */
const val ATTENDANCE_TAB_INDEX = 2

/**
 * Estructura común de las pantallas de check-in: [SaludYaTopBar], contenido desplazable
 * y `BottomNavigationBar` del Design System fijada en "Asistencia".
 *
 * @param title título de la barra superior.
 * @param isLoading muestra un indicador de progreso en lugar de [content].
 */
@Composable
fun CheckInScaffold(
    title: String,
    onBackClick: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = { SaludYaTopBar(title = title, onBackClick = onBackClick) },
        bottomBar = {
            // NOTA: único punto a ajustar si la firma real del Design System difiere.
            BottomNavigationBar(
                selectedIndex = ATTENDANCE_TAB_INDEX,
                onItemSelected = onBottomNavItemSelected,
            )
        },
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                content = content,
            )
        }
    }
}
