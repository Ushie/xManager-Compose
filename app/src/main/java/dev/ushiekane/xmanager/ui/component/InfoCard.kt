package dev.ushiekane.xmanager.ui.component

import android.os.Build
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoCard(
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
                    modifier = Modifier.padding(12.dp, 16.dp, 12.dp, 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CPU/ARCH",
                        color = Color(0xff1DB954),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = Build.SUPPORTED_ABIS.first().uppercase() ,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Divider(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    thickness = 3.dp
                )
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INSTALLED",
                        color = Color(0xff1DB954),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "N/A",
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
                        text = "CHANGELOGS",
                        color = Color(0xff1DB954),
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
            AnimatedVisibility(visible = expandedState) {
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