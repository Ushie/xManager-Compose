package dev.ushiekane.xmanager.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import dev.ushiekane.xmanager.ui.viewmodel.HomeViewModel.Status
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickSettings: () -> Unit, vm: HomeViewModel = getViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text(
                    text = "xManager"
                )
            }, actions = {
                IconButton(onClick = onClickSettings) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            })
        }) { paddingValues ->
        when (vm.status) {
            is Status.Downloading -> {
                DownloadingDialog(
                    release = (vm.status as Status.Downloading).release,
                    onDismiss = vm::dismissDialogAndCancel,
                    onCopy = { vm.copyToClipboard(it) },
                    progress = vm.percentage.toFloat().div(100),
                    total = vm.total,
                    percentage = vm.percentage,
                )
            }

            is Status.Successful -> {
                val file = (vm.status as Status.Successful).file
                SuccessDialog(
                    onDismiss = vm::dismissDialogAndCancel, onInstall = { vm.installApk(file) },
                )
            }

            is Status.Confirm -> {
                val release = (vm.status as Status.Confirm).release
                ConfirmDialog(
                    onDismiss = vm::dismissDialogAndCancel,
                    onDownload = { vm.startDownload(it) },
                    release = release
                )
            }
            // is Status.Existing -> {
            //     ExistingDialog(
            //         onDismiss = vm::dismissDialogAndCancel,
            //         onClickDelete = vm::delete,
            //         onInstall = vm::installApk
            //     )
            // }
            else -> {}
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp, 18.dp, 8.dp, 8.dp)
        ) {
            arrayOf(
                when {
                    vm.prefs.cloned && vm.prefs.experimental -> {
                        Triple(
                            "SE CLONED PATCHED",
                            "Experimental cloned. Unstable",
                            vm.stockClonedExperimentalReleases
                        )
                        Triple(
                            "AE CLONED PATCHED",
                            "Same experimental cloned features. Unstable",
                            vm.amoledClonedExperimentalReleases
                        )
                    }

                    vm.prefs.cloned -> {
                        Triple(
                            "STOCK CLONED PATCHED",
                            "A cloned version of the stock patched",
                            vm.stockClonedReleases
                        )
                        Triple(
                            "AMOLED CLONED PATCHED",
                            "A cloned version of the stock patched with amoled black theme",
                            vm.amoledClonedReleases
                        )
                    }

                    vm.prefs.experimental -> {
                        Triple(
                            "STOCK EXP PATCHED",
                            "Experimental. New features. Unstable.",
                            vm.stockExperimentalReleases
                        )
                        Triple(
                            "AMOLED EXP PATCHED",
                            "Same experimental features but in amoled black theme. Unstable.",
                            vm.amoledExperimentalReleases
                        )
                    }

                    else -> {
                        Triple(
                            "STOCK PATCHED",
                            "Ad free, Unlimited skips and play on-demand",
                            vm.stockReleases
                        )
                        Triple(
                            "AMOLED PATCHED",
                            "Same features but in amoled black theme",
                            vm.amoledReleases
                        )
                    }
                },
                Triple(
                    "LITE PATCHED",
                    "An Ad free, Unlimited skips and play on-demand lightweight experience",
                    vm.liteReleases
                )
            ).forEach { triple ->
                ReleaseCard(
                    title = triple.first,
                    subtitle = triple.second,
                    releases = triple.third,
                    onClick = { vm.confirmDialog(it) },
                    onLongClick = { vm.openDownloadLink(it) })
            }
            InfoCard {
                LazyColumn {
                    vm.changeLog.forEach {
                        item {
                            Text(
                                text = it.changelog,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBDBDBD)
                            )
                            Divider(
                                modifier = Modifier.padding(
                                    vertical = 16.dp, horizontal = 4.dp
                                ), thickness = 3.dp
                            )
                        }
                    }
                }
            }
            ToolsCard(
                onAppInfo = { vm.openAppInfo(it) },
                onLaunch = { vm.openApp(it) },
                onUninstall = { vm.uninstall(it) },
                onDelete = vm::delete,
            )
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialCard("TELEGRAM", R.drawable.telegram)
                SocialCard("REDDIT", R.drawable.reddit)
                SocialCard("SUPPORT", R.drawable.spotify_filled)
                SocialCard("ABOUT", Icons.Default.Info)
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SocialCard("DISCORD", R.drawable.spotify_filled)
                SocialCard("SOURCE", R.drawable.spotify_filled)
                SocialCard("WEBSITE", Icons.Default.Web)
                SocialCard("FAQ", Icons.Default.Help)
            }
        }
    }
}