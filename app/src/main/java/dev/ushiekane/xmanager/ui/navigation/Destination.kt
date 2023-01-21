package dev.ushiekane.xmanager.ui.navigation

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.ushiekane.xmanager.R
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface Destination : Parcelable {
    @Parcelize
    object Home : RootScreenDestination(Icons.Default.Home, Icons.Outlined.Home, R.string.home)

    @Parcelize
    object Settings : RootScreenDestination(Icons.Default.Settings, Icons.Outlined.Settings, R.string.settings)
}
sealed class RootScreenDestination(
    val icon: @RawValue ImageVector,
    val outlinedIcon: @RawValue ImageVector,
    @StringRes val label: Int
): Destination