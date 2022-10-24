package dev.ushiekane.xmanager.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.xinto.taxi.Destination
import dev.ushiekane.xmanager.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed interface AppDestination : Destination {
    @Parcelize
    object Home : AppDestination
}