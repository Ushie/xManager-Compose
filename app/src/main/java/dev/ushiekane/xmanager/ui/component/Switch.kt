package dev.ushiekane.xmanager.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun XManagerSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    scale: Float = 2f,
    width: Dp = 24.dp,
    height: Dp = 12.dp,
    strokeWidth: Dp = 0.8.dp,
    checkedTrackColor: Color = Color(0xFF1B9E4F),
    uncheckedTrackColor: Color = Color(0xFF616161),
) {
    val switchON = remember {
        mutableStateOf(checked)
    }

    val thumbRadius = (height / 2)

    val animatePosition = animateFloatAsState(
        targetValue = if (switchON.value)
            with(LocalDensity.current) { (width - thumbRadius).toPx() }
        else
            with(LocalDensity.current) { (thumbRadius).toPx() }
    )

    Canvas(
        modifier = Modifier
            .size(width = width, height = height)
            .scale(scale = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        onCheckedChange(!switchON.value)
                    }
                )
            }
    ) {
        drawRoundRect(
            color = if (switchON.value) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
        )

        drawRoundRect(
            color = Color(0xFFE6E6E6),
            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )

        drawCircle(
            color = Color(0xFFE6E6E6),
            radius = thumbRadius.toPx(),
            center = Offset(
                x = animatePosition.value,
                y = size.height / 2
            )
        )
    }
}