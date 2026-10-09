package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaludYaScreenLayout(
    header: @Composable () -> Unit,
    selectedDestination: SaludYaDestination,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SaludYaColors.Background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 104.dp)
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content,
        )

        Box(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 34.dp)) {
            header()
        }
    }
}
