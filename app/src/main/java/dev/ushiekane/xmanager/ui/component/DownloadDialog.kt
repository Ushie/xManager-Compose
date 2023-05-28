package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import dev.ushiekane.xmanager.domain.dto.Amoled
import dev.ushiekane.xmanager.domain.dto.AmoledCloned
import dev.ushiekane.xmanager.domain.dto.AmoledClonedExperimental
import dev.ushiekane.xmanager.domain.dto.AmoledExperimental
import dev.ushiekane.xmanager.domain.dto.Lite
import dev.ushiekane.xmanager.domain.dto.Release
import dev.ushiekane.xmanager.domain.dto.Stock
import dev.ushiekane.xmanager.domain.dto.StockCloned
import dev.ushiekane.xmanager.domain.dto.StockClonedExperimental
import dev.ushiekane.xmanager.domain.dto.StockExperimental
import dev.ushiekane.xmanager.ui.theme.Typography

@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    onDownload: (Release) -> Unit,
    release: Release
) {
    val type =
        when (release) {
            is Amoled -> "AMOLED"
            is Stock -> "STOCK"
            is StockCloned -> "STOCK CLONED"
            is AmoledCloned -> "AMOLED CLONED"
            is StockExperimental -> "EXPERIMENTAL"
            is AmoledExperimental -> "EXPERIMENTAL AMOLED"
            is StockClonedExperimental -> "EXPERIMENTAL STOCK CLONED"
            is AmoledClonedExperimental -> "EXPERIMENTAL AMOLED CLONED"
            is Lite -> "LITE"
            else -> "UNKNOWN"
        }
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
                        Pair("VERSION: ", release.version),
                        Pair("PATCHED TYPE: ", type)
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
                    XManagerButton(
                        onClick = { onDownload(release) },
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

@Composable
fun DownloadingDialog(
    release: Release,
    onDismiss: () -> Unit,
    onCopy: (String) -> Unit,
    progress: Float,
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
                                text = "${release.version} | $total MB"
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
                        onClick = { onCopy(release.downloadUrl) },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525)),
                        contentPadding = PaddingValues(18.dp, 4.dp)
                    ) {
                        Text(
                            text = "MIRROR LINK",
                            style = Typography.labelSmall,
                            color = Color.White,
                        )
                    }
                    Spacer(Modifier.weight(1f, true))
                    OutlinedButton(
                        onClick = onDismiss,
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