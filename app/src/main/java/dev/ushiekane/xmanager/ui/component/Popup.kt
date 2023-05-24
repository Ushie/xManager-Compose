package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.ushiekane.xmanager.ui.theme.Typography

@Composable
fun Popup(
    title: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onClickCloned: () -> Unit,
    onClickLite: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            color = Color(0xFF191919),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = title,
                    color = Color(0xFF1DB954),
                    style = Typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onClickLite,
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(width = 1.0.dp, color = Color(0xFF303030))
                    ) {
                        Text(
                            text = "LITE",
                            style = Typography.labelSmall,
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = onClickCloned,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                    ) {
                        Text(
                            text = "CLONED",
                            style = Typography.labelSmall,
                            color = Color.White,
                        )
                    }
                    Button(
                        onClick = onClick,
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252525))
                    ) {
                        Text(
                            text = "RE/AM",
                            style = Typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
