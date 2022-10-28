package dev.ushiekane.xmanager.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReleaseCard(
    title: String,
    subtitle: String,
    latest: String,
    expandedContent: @Composable () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotateState by animateFloatAsState(targetValue = if (expandedState) 180f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
    ) {
        Column(
            modifier = Modifier.clickable { expandedState = !expandedState }) {
            Column {
                Row(
                    modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color(0xff1DB954),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = subtitle,
                        fontStyle = FontStyle.Italic,
                        fontSize = 11.sp,
                        lineHeight = 12.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LATEST",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = latest,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Divider(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    thickness = 3.dp
                )
                Row(
                    modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VERSIONS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.rotate(rotateState),
                        imageVector = Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            AnimatedVisibility(expandedState) {
                Box(
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 12.dp, 12.dp)
                        .height(260.dp)
                ) {
                    expandedContent()
                }
            }
        }
    }
}