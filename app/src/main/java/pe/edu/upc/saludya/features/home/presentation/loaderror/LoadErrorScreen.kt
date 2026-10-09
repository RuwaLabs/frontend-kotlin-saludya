package pe.edu.upc.saludya.features.home.presentation.loaderror

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaStateMessage

@Composable
fun LoadErrorScreen(
    model: LoadErrorUiModel = LoadErrorUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = stringResource(R.string.load_error_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaStateMessage(
            title = stringResource(R.string.load_error_heading),
            description = stringResource(R.string.load_error_description),
            buttonText = stringResource(R.string.load_error_button),
            onButtonClick = onRetryClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun LoadErrorScreenPreview() {
    LoadErrorScreen()
}
