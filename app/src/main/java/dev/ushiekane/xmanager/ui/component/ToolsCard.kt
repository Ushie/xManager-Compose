package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ushiekane.xmanager.ui.component.Dialog.*
import dev.ushiekane.xmanager.util.clonedPackageName
import dev.ushiekane.xmanager.util.litePackageName
import dev.ushiekane.xmanager.util.packageName

@Composable
fun ToolsCard( // TODO: refactor dis
    onLaunch: (String) -> Unit,
    onAppInfo: (String) -> Unit,
    onUninstall: (String) -> Unit,
    onDelete: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(NONE) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF191919))
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (showDialog) {
                    LAUNCH -> {
                        Popup(title = "OPEN PATCHED", onDismiss = { showDialog = NONE }, onClick = {
                            onLaunch(
                                packageName
                            )
                        }, onClickCloned = {
                            onLaunch(
                                clonedPackageName
                            )
                        }, onClickLite = {
                            onLaunch(
                                litePackageName
                            )
                        })
                    }

                    SETTINGS -> {
                        Popup(
                            title = "OPEN SETTINGS",
                            onDismiss = { showDialog = NONE },
                            onClick = {
                                onAppInfo(
                                    packageName
                                )
                            },
                            onClickCloned = {
                                onAppInfo(
                                    clonedPackageName
                                )
                            }, onClickLite = {
                                onAppInfo(
                                    litePackageName
                                )
                            })
                    }

                    UNINSTALL -> {
                        Popup(
                            title = "UNINSTALL PATCHED",
                            onDismiss = { showDialog = NONE },
                            onClick = {
                                onUninstall(
                                    packageName
                                )
                            },
                            onClickCloned = {
                                onUninstall(
                                    clonedPackageName
                                )
                            }, onClickLite = {
                                onUninstall(
                                    litePackageName
                                )
                            })
                    }

                    DELETE -> {
                        onDelete().also { showDialog = NONE }
                    }

                    NONE -> {}
                }
                Text(
                    text = "MANAGER TOOLS",
                    color = Color(0xff1DB954),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                arrayOf(
                    UNINSTALL to Icons.Default.DeleteForever,
                    SETTINGS to Icons.Default.Settings,
                    DELETE to Icons.Default.Refresh,
                    LAUNCH to Icons.Default.Launch
                ).forEach {
                    IconButton(
                        onClick = { showDialog = it.first }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = it.second,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

private enum class Dialog {
    LAUNCH,
    SETTINGS,
    UNINSTALL,
    DELETE,
    NONE
}