package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun Popup(
    title: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onClickCloned: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            color = Color(0xFF212121),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(8.dp)
            ) {
                Box(
                    Modifier
                        .align(Alignment.Start)
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = title,
                        color = Color(0xff1DB954),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Box(modifier = Modifier.align(Alignment.End)) {
                    ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(4.dp),
                            ) {
                                Text(
                                    text = "CLOSE",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Button(
                                onClick = onClickCloned,
                                shape = RoundedCornerShape(4.dp),
                            ) {
                                Text(
                                    text = "CLONED",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Button(
                                onClick = onClick,
                                shape = RoundedCornerShape(4.dp),
                            ) {
                                Text(
                                    text = "RE/AM",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}