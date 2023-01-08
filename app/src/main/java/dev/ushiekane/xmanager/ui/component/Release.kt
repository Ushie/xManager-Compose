package dev.ushiekane.xmanager.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.ushiekane.xmanager.R
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Release(
    releaseVersion: String,
    releaseArch: String,
    releaseLink: String,
    isLatest: Boolean,
    isAmoled: Boolean,
    onClick: () -> Unit,
    onLongClick: (String) -> Unit,
) {
    Row(modifier = Modifier
        .combinedClickable(onClick = onClick, onLongClick = { onLongClick(releaseLink) })
        .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (isAmoled) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.spotify_outlined),
                contentDescription = null,
                tint = Color.Unspecified
            )
        } else {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.spotify_filled),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        if (isLatest) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFFFF1744))) {
                        append("[LATEST] ")
                    }
                    append("$releaseVersion ($releaseArch)")
                }, fontSize = 12.sp, fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFFBDBDBD))) {
                        append("[OLDER] ")
                    }
                    append("$releaseVersion ($releaseArch)")
                }, fontSize = 12.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}