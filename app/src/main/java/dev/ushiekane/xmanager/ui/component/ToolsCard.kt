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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ushiekane.xmanager.ui.component.Dialog.*
import dev.ushiekane.xmanager.util.*

@Composable
fun ToolsCard(
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(NONE) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (showDialog) {
                    LAUNCH -> {
                        Popup(title = "OPEN PATCHED", onDismiss = { showDialog = NONE }, onClick = {
                            context.openApp(
                                packageName
                            )
                        }, onClickCloned = {
                            context.openApp(
                                clonedPackageName
                            )
                        })
                    }
                    SETTINGS -> {
                        Popup(
                            title = "OPEN SETTINGS",
                            onDismiss = { showDialog = NONE },
                            onClick = {
                                context.openAppInfo(
                                    packageName
                                )
                            },
                            onClickCloned = {
                                context.openAppInfo(
                                    clonedPackageName
                                )
                            })
                    }
                    UNINSTALL -> {
                        Popup(
                            title = "UNINSTALL PATCHED",
                            onDismiss = { showDialog = NONE },
                            onClick = {
                                context.uninstall(
                                    packageName
                                )
                            },
                            onClickCloned = {
                                context.uninstall(
                                    clonedPackageName
                                )
                            })
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
                IconButton(
                    onClick = { showDialog = UNINSTALL }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { showDialog = SETTINGS }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { context.deleteCache() }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = { showDialog = LAUNCH }
                ) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.Launch,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

private enum class Dialog {
    LAUNCH,
    SETTINGS,
    UNINSTALL,
    NONE
}