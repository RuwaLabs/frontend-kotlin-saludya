package pe.edu.upc.saludya.main

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController

@Composable
fun MainNavigationBar(navController: NavController) {

    var selectedItem by rememberSaveable {
        mutableStateOf(NavigationItem.HOME)
    }

    BottomAppBar {
        NavigationItem.entries.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(NavigationItem.HOME.route) {
                            saveState = true
                        }
                        restoreState = true
                    }
                    selectedItem = item
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title)
                    )
                },
                label = {
                    Text(text = stringResource(item.title))
                }
            )
        }
    }
}
