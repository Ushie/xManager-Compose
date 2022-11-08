package dev.ushiekane.xmanager.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ushiekane.xmanager.R
import dev.ushiekane.xmanager.ui.component.*
import dev.ushiekane.xmanager.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onClickSettings: () -> Unit,
    viewModel: HomeViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "xManager"
                    )
                },
                actions = {
                    IconButton(onClick = onClickSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val uwu = false
            ReleaseCard(
                title = "SPOTIFY (REGULAR)",
                subtitle = "Unlimited skips, play on-demand, ad-free and new features!",
                latest = viewModel.latestNormalRelease,
                expandedContent = {
                    if (uwu) {
                        LazyColumn {
                            viewModel.normalReleasesList.forEach {
                                item { Release(it.version, it.downloadUrl, it.arch, false) }
                            }
                        }
                    } else {
                        LazyColumn {
                            viewModel.normalClonedReleasesList.forEach {
                                item { Release(it.version, it.downloadUrl, it.arch, false) }
                            }
                        }
                    }
                }
            )
            ReleaseCard(
                title = "SPOTIFY (AMOLED)",
                subtitle = "Same features as regular but in amoled black version!",
                latest = viewModel.latestAmoledRelease,
                expandedContent = {
                    if (uwu) {
                        LazyColumn {
                            viewModel.amoledReleasesList.forEach {
                                item { Release(it.version, it.downloadUrl, it.arch, true) }
                            }
                        }
                    } else {
                        LazyColumn {
                            viewModel.amoledClonedReleasesList.forEach {
                                item { Release(it.version, it.downloadUrl, it.arch, true) }
                            }
                        }
                    }
                }
            )
            InfoCard {
                LazyColumn {
                    viewModel.changeLog.forEach {
                        item {
                            Text(
                                text = it.changelog,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBDBDBD)
                            )
                            Divider(
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp),
                                thickness = 3.dp
                            )
                        }
                    }
                }
            }
            ToolsCard()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialCard("TELEGRAM", R.drawable.telegram)
                SocialCard("REDDIT", R.drawable.reddit)
                SocialCard("SUPPORT", R.drawable.spotify_filled)
                SocialCard("ABOUT", Icons.Default.Info)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialCard("DISCORD", R.drawable.spotify_filled)
                SocialCard("SOURCE", R.drawable.spotify_filled)
                SocialCard("WEBSITE", Icons.Default.Web)
                SocialCard("FAQ", Icons.Default.Help)
            }
        }
    }
}