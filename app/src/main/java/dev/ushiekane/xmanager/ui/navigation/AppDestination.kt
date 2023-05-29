package dev.ushiekane.xmanager.ui.navigation

import com.xinto.taxi.Destination
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AppDestination: Parcelable {
    @Parcelize
    object Home : AppDestination

    @Parcelize
    object Settings : AppDestination
}
