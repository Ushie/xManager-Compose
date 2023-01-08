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
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
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
                    onDismiss = vm::cancel,
                    onFixer = { vm.fixer(vm.selectedRelease!!) },
                    onCancel = vm::cancel,
                    progress = vm.progress.toDouble().div(100).toFloat(),
                    downloaded = vm.downloaded,
                    total = vm.total,
                    percentage = vm.progress,
                )
            }
            is Status.Successful -> {
                SuccessDialog(
                    onDismiss = vm::cancel, onInstall = vm::installApk
                )
            }
            is Status.Confirm -> {
                ConfirmDialog(
                    onDismiss = vm::hideDialog,
                    onDownload = vm::startDownload,
                    onCopy = { vm.copyToClipboard(vm.selectedRelease!!) },
                    latest = vm.isLatest,
                    isAmoled = vm.isAmoled,
                    releaseArch = vm.selectedRelease!!.arch,
                    releaseVersion = vm.selectedRelease!!.version,
                )
            }
            is Status.Existing -> {
                ExistingDialog(
                    onDismiss = vm::showConfirmDialog,
                    onClickDelete = vm::delete,
                    onInstall = vm::installApk
                )
            }
            else -> {}
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp, 18.dp, 8.dp, 8.dp)
        ) {
            val uwu = false
            ReleaseCard(title = "SPOTIFY (REGULAR)",
                subtitle = "Unlimited skips, play on-demand, ad-free and new features!",
                latest = vm.latestNormalRelease,
                expandedContent = {
                    if (uwu) {
                        LazyColumn {
                            vm.normalReleasesList.forEach {
                                item {
                                    Release(it.version,
                                        it.arch,
                                        it.downloadUrl,
                                        it.version == vm.latestNormalRelease,
                                        false,
                                        onClick = { vm.checkIfExisting(it) },
                                        onLongClick = { vm.openDownloadLink(it) })
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            vm.normalClonedReleasesList.forEach {
                                item {
                                    Release(it.version,
                                        it.arch,
                                        it.downloadUrl,
                                        it.version == vm.latestNormalRelease,
                                        false,
                                        onClick = { vm.checkIfExisting(it) },
                                        onLongClick = { vm.openDownloadLink(it) })
                                }
                            }
                        }
                    }
                })
            ReleaseCard(title = "SPOTIFY (AMOLED)",
                subtitle = "Same features as regular but in amoled black version!",
                latest = vm.latestAmoledRelease,
                expandedContent = {
                    if (uwu) {
                        LazyColumn {
                            vm.amoledReleasesList.forEach {
                                item {
                                    Release(it.version,
                                        it.arch,
                                        it.downloadUrl,
                                        vm.amoledReleasesList.indexOf(it) == 0,
                                        true,
                                        onClick = { vm.checkIfExisting(it) },
                                        onLongClick = { vm.openDownloadLink(it) })
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            vm.amoledClonedReleasesList.forEach {
                                item {
                                    Release(it.version,
                                        it.arch,
                                        it.downloadUrl,
                                        vm.amoledClonedReleasesList.indexOf(it) == 0,
                                        true,
                                        onClick = { vm.checkIfExisting(it) },
                                        onLongClick = { vm.openDownloadLink(it) })
                                }
                            }
                        }
                    }
                })
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