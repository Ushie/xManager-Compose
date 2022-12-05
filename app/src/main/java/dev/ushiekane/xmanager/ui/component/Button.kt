package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun XManagerButton(
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(6.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFF373737)),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .sizeIn(minWidth = 90.dp, minHeight = 16.dp)
                .padding(4.dp)
        ) {
            content()
        }
    }
}
@Composable
fun XManagerOutlinedButton(
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(6.dp),
    border: BorderStroke? = BorderStroke(width = 1.5.dp, color = Color(0xFF303030)),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (RowScope.() -> Unit)
) {
    OutlinedButton(
        onClick = onClick,
        shape = shape,
        border = border,
        contentPadding = contentPadding
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .sizeIn(minWidth = 90.dp, minHeight = 16.dp)
                .padding(4.dp)
        ) {
            content()
        }
    }
}