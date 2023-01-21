package dev.ushiekane.xmanager.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.ushiekane.xmanager.ui.navigation.Destination
import dev.ushiekane.xmanager.ui.navigation.RootScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreen(
    bottomNavItems: List<RootScreenDestination>,
    currentDestination: Destination,
    onNavClick: (Destination) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach {
                    NavigationBarItem(
                        selected = it == currentDestination,
                        icon = { Icon(imageVector = if (it == currentDestination) it.icon else it.outlinedIcon, contentDescription = null) },
                        label = { Text(stringResource(id = it.label))},
                        onClick = { if (it != currentDestination) onNavClick(it) })
                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()) {
            content()
        }
    }
}