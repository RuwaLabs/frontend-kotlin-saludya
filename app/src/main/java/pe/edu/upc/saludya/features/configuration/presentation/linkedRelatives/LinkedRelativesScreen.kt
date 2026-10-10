package pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.icon.addCircleOutlineIcon
import pe.edu.upc.saludya.core.desingsystem.icon.arrowBack
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.presentation.components.LinkedMinorCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription

@Composable
fun LinkedRelativesScreen(
    viewModel: LinkedRelativesViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onViewMinorClick: (String) -> Unit = {},
    onAddMinorClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = stringResource(R.string.linked_relatives_title),
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
                is LinkedRelativesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LinkedRelativesUiState.Error -> {
                    Text(
                        text = stringResource(state.messageRes),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is LinkedRelativesUiState.Success -> {
                    when {
                        state.minors.isEmpty() -> {
                            LinkedRelativesEmptyContent(onAddMinorClick = onAddMinorClick)
                        }
                        state.justSynced -> {
                            LinkedRelativesJustSyncedContent(
                                minors = state.minors,
                                onViewMinorClick = onViewMinorClick,
                                onAddMinorClick = onAddMinorClick,
                            )
                        }
                        else -> {
                            LinkedRelativesContent(
                                minors = state.minors,
                                onViewMinorClick = onViewMinorClick,
                                onAddMinorClick = onAddMinorClick,
                                onSyncClick = viewModel::markAsSynced,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LinkedRelativesEmptyContent(
    onAddMinorClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Icon(
            imageVector = addCircleOutlineIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.linked_relatives_empty_title),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 30.sp,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.linked_relatives_empty_description),
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = stringResource(R.string.linked_relatives_add),
            onClick = onAddMinorClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LinkedRelativesJustSyncedContent(
    minors: List<LinkedMinor>,
    onViewMinorClick: (String) -> Unit,
    onAddMinorClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "âœ“",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.linked_relatives_just_synced),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        minors.forEach { minor ->
            JustSyncedMinorCard(
                fullName = minor.fullName,
                onViewDetailsClick = { onViewMinorClick(minor.id) },
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = stringResource(R.string.linked_relatives_add),
            onClick = onAddMinorClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun JustSyncedMinorCard(
    fullName: String,
    onViewDetailsClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Text(
                text = fullName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.linked_relatives_just_synced_status),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.clickable(onClick = onViewDetailsClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.linked_relatives_view_data),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = arrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(180f),
                )
            }
        }
    }
}

@Composable
private fun LinkedRelativesContent(
    minors: List<LinkedMinor>,
    onViewMinorClick: (String) -> Unit,
    onAddMinorClick: () -> Unit,
    onSyncClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenDescription(
            text = stringResource(R.string.linked_relatives_description),
        )

        minors.forEach { minor ->
            LinkedMinorCard(
                fullName = minor.fullName,
                age = minor.age,
                onViewDetailsClick = { onViewMinorClick(minor.id) },
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            text = stringResource(R.string.linked_relatives_add),
            onClick = onAddMinorClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.linked_relatives_sync),
            onClick = onSyncClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedRelativesScreen - VacÃ­o")
@Composable
private fun LinkedRelativesEmptyPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedRelativesEmptyContent(onAddMinorClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedRelativesScreen - ReciÃ©n sincronizado")
@Composable
private fun LinkedRelativesJustSyncedPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedRelativesJustSyncedContent(
            minors = listOf(
                LinkedMinor(
                    id = "1",
                    fullName = "Mateo Torres",
                    age = 8,
                    documentType = "DNI",
                    documentNumberMasked = "••••2716",
                    birthDate = "12 abr 2018",
                    representativeName = "Lucía Torres",
                    isVerified = true,
                ),
            ),
            onViewMinorClick = {},
            onAddMinorClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedRelativesScreen - Con datos")
@Composable
private fun LinkedRelativesContentPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedRelativesContent(
            minors = listOf(
                LinkedMinor(
                    id = "1",
                    fullName = "Mateo Torres",
                    age = 8,
                    documentType = "DNI",
                    documentNumberMasked = "••••2716",
                    birthDate = "12 abr 2018",
                    representativeName = "Lucía Torres",
                    isVerified = true,
                ),
            ),
            onViewMinorClick = {},
            onAddMinorClick = {},
            onSyncClick = {},
        )
    }
}