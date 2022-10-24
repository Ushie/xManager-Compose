package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToolsCard(
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MANAGER TOOLS",
                    color = Color(0xff1DB954),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(modifier = Modifier.size(28.dp), imageVector = Icons.Default.DeleteForever, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(modifier = Modifier.size(28.dp), imageVector = Icons.Default.Settings, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(modifier = Modifier.size(28.dp), imageVector = Icons.Default.Refresh, contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(modifier = Modifier.size(28.dp), imageVector = Icons.Default.Launch, contentDescription = null)
                }
            }
        }
    }
}