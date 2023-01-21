package dev.ushiekane.xmanager.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface Destination: Parcelable {

    @Parcelize
    object Home : Destination

    @Parcelize
    object Settings : Destination
}