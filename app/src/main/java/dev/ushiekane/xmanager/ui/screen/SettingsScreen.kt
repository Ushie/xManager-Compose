package dev.ushiekane.xmanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.taxi.BackstackNavigator
import dev.ushiekane.xmanager.ui.component.ListItems
import dev.ushiekane.xmanager.ui.navigation.AppDestination
import dev.ushiekane.xmanager.ui.viewmodel.SettingsViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigator: BackstackNavigator<AppDestination>,
    viewModel: SettingsViewModel = getViewModel()
) {
    val prefs = viewModel.prefs

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            ListItems(
                title = "CLONED VERSION (BETA)",
                subtitle = "Enabling this will allow you to download and install the cloned version of the patched application.\n\nThis will also resolve most of the installation errors or problems, especially if you have a pre-installed Spotify application.",
                checked = prefs.useClone,
                onCheckedChange = { prefs.useClone = it }
            )
            ListItems(
                title = "LIST AUTO-REFRESH",
                subtitle = "Enabling this will automatically refresh the list everytime you launch the application.\n\nYou can manually refresh the list by dragging the main screen downward.",
                checked = prefs.autoRefresh,
                onCheckedChange = { prefs.autoRefresh = it }
            )
            ListItems(
                title = "FORCE AUTO-INSTALL",
                subtitle = "Enabling this will automatically install the patched application and update once downloaded.",
                checked = prefs.autoInstall,
                onCheckedChange = { prefs.autoInstall = it }
            )
            ListItems(
                title = "DISABLE REWARDED ADS",
                subtitle = "We know that most of us does not like ads but in our case, this significantly help us to fund our database, hosting links, updates, more patches and daily needs.\n\nThis is the simpliest way to support us without donating or spending anything." ,
                checked = prefs.hideAds,
                onCheckedChange = { prefs.hideAds = it }
            )
        }
    }
}