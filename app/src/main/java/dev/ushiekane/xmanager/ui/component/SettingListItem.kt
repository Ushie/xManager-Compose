package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ushiekane.xmanager.ui.theme.CustomFont

@Composable
fun SettingListItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 8.dp, 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF212121))
    ) {
        Column {
            Row(
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = CustomFont,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1DB954),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier.height(35.dp),
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            }
            Row(
                modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subtitle,
                    fontFamily = CustomFont,
                    lineHeight = 13.sp,
                    fontSize = 12.sp
                )
            }
        }
    }
}