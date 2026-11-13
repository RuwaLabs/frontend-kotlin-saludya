package pe.edu.upc.saludya.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

@Composable
fun MainNavigationBar(navController: NavController) {

    var selectedItem by rememberSaveable {
        mutableStateOf(NavigationItem.HOME)
    }

    val currentEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(currentEntry) {
        val destination = currentEntry?.destination ?: return@LaunchedEffect
        val match = NavigationItem.entries.firstOrNull { item ->
            val graphName = item.route::class.simpleName.orEmpty()
            destination.hierarchy.any { it.route?.contains(graphName) == true }
        }
        if (match != null) selectedItem = match
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .selectableGroup()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigationItem.entries.forEach { item ->
                NavigationTab(
                    item = item,
                    selected = selectedItem == item,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.id) {
                                inclusive = false
                            }
                        }
                        selectedItem = item
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun NavigationTab(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val background = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val label = stringResource(item.title)

    Column(
        modifier = modifier
            .clip(shape)
            .background(background)
            .clickable(
                role = Role.Tab,
                onClick = onClick,
            )
            .semantics { this.selected = selected }
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = contentColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainNavigationBarHomePreview() {
    SaludYaTheme(dynamicColor = false) {
        MainNavigationBarPreviewContent(selected = NavigationItem.HOME)
    }
}

@Preview(showBackground = true)
@Composable
private fun MainNavigationBarSettingsPreview() {
    SaludYaTheme(dynamicColor = false) {
        MainNavigationBarPreviewContent(selected = NavigationItem.SETTINGS)
    }
}

@Composable
private fun MainNavigationBarPreviewContent(selected: NavigationItem) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavigationItem.entries.forEach { item ->
                NavigationTab(
                    item = item,
                    selected = selected == item,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}