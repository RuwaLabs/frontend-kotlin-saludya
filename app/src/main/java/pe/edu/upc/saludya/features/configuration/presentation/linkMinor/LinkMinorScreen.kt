package pe.edu.upc.saludya.features.configuration.presentation.linkMinor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.presentation.components.DocumentTypeDropdown
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription

@Composable
fun LinkMinorScreen(
    viewModel: LinkMinorViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onVerified: (String, String) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is LinkMinorUiState.Verified) {
            onVerified(state.documentType, state.documentNumber)
        }
    }

    val documentTypes = listOf(
        stringResource(R.string.doc_type_dni),
        stringResource(R.string.doc_type_foreign),
        stringResource(R.string.doc_type_passport),
    )

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = stringResource(R.string.link_minor_title),
                onBackClick = onBackClick,
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        when (val state = uiState) {
            is LinkMinorUiState.Error -> {
                Text(
                    text = stringResource(state.messageRes),
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(24.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            is LinkMinorUiState.Verified -> {
                // Manejado por el LaunchedEffect
            }
            is LinkMinorUiState.Editing -> {
                LinkMinorContent(
                    modifier = Modifier.padding(innerPadding),
                    documentType = state.documentType,
                    documentNumber = state.documentNumber,
                    isVerifying = state.isVerifying,
                    documentTypes = documentTypes,
                    onDocumentTypeChange = viewModel::onDocumentTypeChange,
                    onDocumentNumberChange = viewModel::onDocumentNumberChange,
                    onVerify = viewModel::verifyDocument,
                )
            }
        }
    }
}

@Composable
private fun LinkMinorContent(
    modifier: Modifier = Modifier,
    documentType: String,
    documentNumber: String,
    isVerifying: Boolean,
    documentTypes: List<String>,
    onDocumentTypeChange: (String) -> Unit,
    onDocumentNumberChange: (String) -> Unit,
    onVerify: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScreenDescription(
            text = stringResource(R.string.link_minor_description),
        )

        Spacer(modifier = Modifier.height(20.dp))

        DocumentTypeDropdown(
            label = stringResource(R.string.link_minor_doc_type),
            selected = documentType,
            options = documentTypes,
            onSelected = onDocumentTypeChange,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.link_minor_doc_number),
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = documentNumber,
            onValueChange = onDocumentNumberChange,
            placeholder = stringResource(R.string.link_minor_doc_number_placeholder),
            keyboardType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(
                if (isVerifying) R.string.link_minor_verifying
                else R.string.link_minor_verify
            ),
            onClick = onVerify,
            enabled = !isVerifying,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkMinorScreen")
@Composable
private fun LinkMinorScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkMinorContent(
            documentType = "DNI",
            documentNumber = "••••2716",
            isVerifying = false,
            documentTypes = listOf("DNI", "Carné de extranjería", "Pasaporte"),
            onDocumentTypeChange = {},
            onDocumentNumberChange = {},
            onVerify = {},
        )
    }
}