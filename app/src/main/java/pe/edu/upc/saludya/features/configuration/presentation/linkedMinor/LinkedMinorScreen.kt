package pe.edu.upc.saludya.features.configuration.presentation.linkedMinor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.presentation.components.ConfirmDialog
import pe.edu.upc.saludya.features.configuration.presentation.components.MinorDetailCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

@Composable
fun LinkedMinorScreen(
    minorId: String,
    viewModel: LinkedMinorViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onViewAppointmentsClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onUnlinkConfirmed: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(minorId) {
        viewModel.loadMinor(minorId)
    }

    LaunchedEffect(uiState) {
        if (uiState is LinkedMinorUiState.Unlinked) {
            onUnlinkConfirmed()
        }
    }

    // Modal: "Â¿Dejar esta vinculaciÃ³n?" (early return)
    val current = uiState
    if (current is LinkedMinorUiState.ConfirmingUnlink) {
        ConfirmDialog(
            title = stringResource(R.string.linked_minor_unlink_confirm_title),
            message = stringResource(
                R.string.linked_minor_unlink_confirm_message,
                current.minor.fullName.split(" ").first(),
            ),
            confirmText = stringResource(R.string.linked_minor_unlink_confirm_button),
            cancelText = stringResource(R.string.linked_minor_unlink_cancel),
            onConfirm = viewModel::confirmUnlink,
            onCancel = viewModel::cancelUnlink,
        )
        return
    }

    // Error inesperado (early return)
    if (current is LinkedMinorUiState.Error) {
        Text(
            text = stringResource(current.messageRes),
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.error,
        )
        return
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = stringResource(R.string.linked_minor_title),
                onBackClick = onBackClick,
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                is LinkedMinorUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LinkedMinorUiState.Success -> {
                    LinkedMinorContent(
                        minor = state.minor,
                        onViewAppointmentsClick = onViewAppointmentsClick,
                        onSyncClick = onSyncClick,
                        onUnlinkClick = viewModel::requestUnlink,
                    )
                }
                else -> {
                    // Estados ya manejados con early return o por LaunchedEffect
                }
            }
        }
    }
}

@Composable
private fun LinkedMinorContent(
    minor: LinkedMinor,
    onViewAppointmentsClick: () -> Unit,
    onSyncClick: () -> Unit,
    onUnlinkClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MinorDetailCard(
            fullName = minor.fullName,
            documentType = minor.documentType,
            documentNumberMasked = minor.documentNumberMasked,
            birthDate = minor.birthDate,
            representativeName = minor.representativeName,
            isVerified = minor.isVerified,
        )

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            text = stringResource(R.string.linked_minor_view_appointments),
            onClick = onViewAppointmentsClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.linked_minor_sync),
            onClick = onSyncClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.linked_minor_unlink),
            onClick = onUnlinkClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedMinorScreen")
@Composable
private fun LinkedMinorScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedMinorContent(
            minor = LinkedMinor(
                id = "1",
                fullName = "Mateo Torres",
                age = 8,
                documentType = "DNI",
                documentNumberMasked = "••••2716",
                birthDate = "12 abr 2018",
                representativeName = "Lucía Torres",
                isVerified = true,
            ),
            onViewAppointmentsClick = {},
            onSyncClick = {},
            onUnlinkClick = {},
        )
    }
}