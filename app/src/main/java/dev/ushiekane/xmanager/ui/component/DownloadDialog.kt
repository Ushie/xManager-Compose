package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.ushiekane.xmanager.ui.component.DownloadStatus.*
import dev.ushiekane.xmanager.ui.theme.CustomFont
import dev.ushiekane.xmanager.ui.theme.Typography
import dev.ushiekane.xmanager.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun DownloadDialog(
    onDismiss: () -> Unit, home: HomeViewModel = getViewModel(), releaseLink: String
) {
    home.downloadApk(releaseLink)
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color(0xFF212121), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(8.dp)
            ) {
                when (home.status) {
                    DOWNLOADING -> {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = "DOWNLOADING FILE...",
                            color = Color(0xFF1DB954),
                            style = Typography.titleMedium
                        )
                        Column(
                            Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                home.progress.toFloat(),
                                modifier = Modifier
                                    .height(4.dp)
                                    .fillMaxWidth(),
                                color = Color(0xFF1DB954),
                                trackColor = Color.Transparent
                            )
                            Row(Modifier.fillMaxWidth()) {
                                Text(text = "%" + home.progress, color = Color.White)
                                Spacer(Modifier.weight(1f, true))
                                Text("${home.downloaded} MB | ${home.total} MB")
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Button(
                                onClick = { home.fixer(releaseLink) },
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(width = 1.0.dp, color = Color(0xFF303030)),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                            ) {
                                Text(
                                    text = "FIXER",
                                    style = Typography.labelSmall,
                                    color = Color.Yellow,
                                )
                            }
                            Spacer(Modifier.weight(1f, true))
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                            ) {
                                Text(
                                    text = "CANCEL",
                                    style = Typography.labelSmall,
                                    color = Color(0xFF1DB954),
                                )
                            }
                        }
                    }
                    CANCELLED -> {
                        onDismiss()
                    }
                    FAILED -> {
                        onDismiss()
                    }
                    SUCCESSFUL -> {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = "SUCCESSFULLY DOWNLOADED",
                            textAlign = TextAlign.Left,
                            color = Color(0xFF1DB954),
                            fontFamily = CustomFont,
                            style = Typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                            ) {
                                Text(
                                    text = "LATER",
                                    style = Typography.labelSmall,
                                    color = Color.Yellow,
                                )
                            }
                            Spacer(Modifier.weight(1f, true))
                            Button(
                                onClick = { onDismiss(); home.installApk() },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                            ) {
                                Text(
                                    text = "INSTALL NOW",
                                    style = Typography.labelSmall,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class DownloadStatus {
    DOWNLOADING, CANCELLED, FAILED, SUCCESSFUL
}