package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.ushiekane.xmanager.ui.theme.Typography


@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    onCopy: () -> Unit,
    latest: Boolean,
    isAmoled: Boolean,
    releaseArch: String,
    releaseVersion: String,
) {
    val latestOrNot = if (latest) "LATEST VERSION" else "OLDER VERSION"
    val amoled = if (isAmoled) "AMOLED" else "REGULAR"
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color(0xFF232323), shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "PATCHED INFORMATION",
                        color = Color(0xFF1DB954),
                        style = Typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    listOf(
                        Pair("RELEASE: ", latestOrNot),
                        Pair("VERSION: ", releaseVersion),
                        Pair("CPU/ARCH: ", releaseArch),
                        Pair("PATCHED TYPE: ", amoled)
                    ).forEach { (first, second) ->
                        Text(buildAnnotatedString {
                            append(first)
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append(second)
                            }
                        })
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text =
                        "Downloading this patched apk will overwrite the previous file located at external directory.",
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    XManagerOutlinedButton(
                        onClick = onDismiss,
                    ) {
                        Text(
                            text = "CANCEL",
                            style = Typography.labelSmall,
                            color = Color(0xFF1DB954)
                        )
                    }
                    Spacer(Modifier.weight(1f, true))
                    Row {
                        XManagerButton(
                            onClick = onCopy,
                        ) {
                            Text(
                                text = "COPY URL",
                                style = Typography.labelSmall,
                                color = Color(0xFF1DB954),
                            )
                        }
                        Spacer(Modifier.width(2.dp))
                        XManagerButton(
                            onClick = onDownload,
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = "DOWNLOAD",
                                style = Typography.labelSmall,
                                color = Color(0xFF1DB954),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DownloadingDialog(
    onDismiss: () -> Unit,
    onFixer: () -> Unit,
    onCancel: () -> Unit,
    progress: Float,
    downloaded: Double,
    total: Double,
    percentage: Int,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color(0xFF212121), shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "DOWNLOADING FILE...",
                        color = Color(0xFF1DB954),
                        style = Typography.titleMedium
                    )
                    Spacer(Modifier.height(32.dp))
                    Column(
                        Modifier.fillMaxWidth()
                    ) {
                        ProgressIndicator(
                            progress,
                            modifier = Modifier
                                .height(10.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            color = Color(0xFF1DB954),
                            trackColor = Color.Transparent
                        )
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                text = "$percentage%"
                            )
                            Spacer(Modifier.weight(1f, true))
                            Text(
                                text = "$downloaded MB | $total MB"
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Button(
                        onClick = onFixer,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                        contentPadding = PaddingValues(18.dp, 4.dp)
                    ) {
                        Text(
                            text = "FIXER",
                            style = Typography.labelSmall,
                            color = Color.Yellow,
                        )
                    }
                    Spacer(Modifier.weight(1f, true))
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(width = 1.5.dp, color = Color(0xFF303030)),
                        contentPadding = PaddingValues(18.dp, 4.dp)
                    ) {
                        Text(
                            text = "CANCEL",
                            style = Typography.labelSmall,
                            color = Color(0xFF1DB954)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    onInstall: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color(0xFF212121), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = "SUCCESSFULLY\nDOWNLOADED",
                    textAlign = TextAlign.Left,
                    color = Color(0xFF1DB954),
                    style = Typography.titleMedium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(width = 1.5.dp, color = Color(0xFF303030)),
                        contentPadding = PaddingValues(24.dp, 4.dp)
                    ) {
                        Text(
                            text = "LATER",
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp),
                            style = Typography.labelSmall,
                            color = Color.White,
                        )
                    }
                    Spacer(Modifier.weight(1f, true))
                    Button(
                        onClick = onInstall,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                        contentPadding = PaddingValues(24.dp, 4.dp)
                    ) {
                        Text(
                            text = "INSTALL NOW",
                            modifier = Modifier.padding(start = 2.dp, end = 2.dp),
                            style = Typography.labelSmall,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExistingDialog(
    onDismiss: () -> Unit,
    onClickDelete: () -> Unit,
    onInstall: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color(0xFF212121), shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 20.dp, 0.dp)
                ) {
                    Text(
                        text = "EXISTING PATCHED",
                        color = Color(0xFF1DB954),
                        style = Typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text =
                        "An existing patched apk found in one of the directory. What action would you like to do?",
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(width = 1.5.dp, color = Color(0xFF303030)),
                        contentPadding = PaddingValues(18.dp, 4.dp)
                    ) {
                        Text(
                            text = "IGNORE",
                            style = Typography.labelSmall,
                            color = Color(0xFF1DB954)
                        )
                    }
                    Spacer(Modifier.weight(1f, true))
                    Row {
                        Button(
                            onClick = { onClickDelete(); onDismiss() },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                            contentPadding = PaddingValues(18.dp, 4.dp)
                        ) {
                            Text(
                                text = "DELETE",
                                style = Typography.labelSmall,
                                color = Color(0xFF1DB954),
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        Button(
                            onClick = onInstall,
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = "INSTALL",
                                style = Typography.labelSmall,
                                color = Color(0xFF1DB954),
                            )
                        }
                    }
                }
            }
        }
    }
}