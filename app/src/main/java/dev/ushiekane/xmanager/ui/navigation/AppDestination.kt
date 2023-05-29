package dev.ushiekane.xmanager.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AppDestination: Parcelable {
    @Parcelize
    object Home : AppDestination

    @Parcelize
    object Settings : AppDestination
}
