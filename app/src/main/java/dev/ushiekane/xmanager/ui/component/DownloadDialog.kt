package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.ushiekane.xmanager.ui.theme.Typography
import dev.ushiekane.xmanager.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun DownloadDialog(
    onDismiss: () -> Unit, home: HomeViewModel = getViewModel(), releaseLink: String, releaseName: String
) {
    fun close() {
        home.downloadStatus = HomeViewModel.Status.Idle
        onDismiss()
    }
    when (home.downloadStatus) {
        HomeViewModel.Status.Downloading -> {
            Dialog(onDismissRequest = { close() }) {
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
                            text = "DOWNLOADING FILE...",
                            color = Color(0xFF1DB954),
                            style = Typography.titleMedium
                        )
                        Column(
                            Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                home.calculateBar(home.downloadedSize, home.totalSize),
                                modifier = Modifier
                                    .height(10.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                color = Color(0xFF1DB954),
                                trackColor = Color.Transparent
                            )
                            Row(Modifier.fillMaxWidth()) {
                                Text(
                                    text = "${
                                        home.calculatePercentage(
                                            home.downloadedSize,
                                            home.totalSize
                                        )
                                    }%"
                                )
                                Spacer(Modifier.weight(1f, true))
                                Text(
                                    text = "${home.calculateSize(home.downloadedSize)} MB | ${
                                        home.calculateSize(
                                            home.totalSize
                                        )
                                    } MB"
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Button(
                                onClick = { home.fixer(releaseLink) },
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(
                                    width = 1.0.dp,
                                    color = Color(0xFF303030)
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF252525)
                                )
                            ) {
                                Text(
                                    text = "FIXER",
                                    style = Typography.labelSmall,
                                    color = Color.Yellow,
                                )
                            }
                            Spacer(Modifier.weight(1f, true))
                            Button(
                                onClick = {home.cancel(); close()},
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF252525)
                                )
                            ) {
                                Text(
                                    text = "CANCEL",
                                    style = Typography.labelSmall,
                                    color = Color(0xFF1DB954),
                                )
                            }
                        }
                    }
                }
            }
        }
        HomeViewModel.Status.Idle -> {
        }
        HomeViewModel.Status.Failed -> {
        }
        HomeViewModel.Status.Successful -> {
            Dialog(onDismissRequest = { close() }) {
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { close() },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF252525)
                                )
                            ) {
                                Text(
                                    text = "LATER",
                                    style = Typography.labelSmall,
                                    color = Color.Yellow,
                                )
                            }
                            Spacer(Modifier.weight(1f, true))
                            Button(
                                onClick = { home.installApk(); close() },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF252525)
                                )
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
        HomeViewModel.Status.Confirm ->  {

        }
    }
}