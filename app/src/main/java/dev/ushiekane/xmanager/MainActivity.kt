package dev.ushiekane.xmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.isSystemInDarkTheme
import dev.olshevski.navigation.reimagined.*
import dev.ushiekane.xmanager.preferences.PreferencesManager
import dev.ushiekane.xmanager.ui.navigation.Destination
import dev.ushiekane.xmanager.ui.navigation.RootScreenDestination
import dev.ushiekane.xmanager.ui.screen.RootScreen
import dev.ushiekane.xmanager.ui.theme.Theme
import dev.ushiekane.xmanager.ui.theme.XManagerTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    val prefs: PreferencesManager by inject()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XManagerTheme(
                darkTheme = prefs.theme == Theme.DARK || prefs.theme == Theme.SYSTEM && isSystemInDarkTheme(),
                dynamicColor = prefs.dynamicColor
            ) {
                val navController = rememberNavController<Destination>(startDestination = Destination.Home)

                NavBackHandler(navController)

                AnimatedNavHost(
                    controller = navController,
                    transitionSpec = { _, _, _ -> fadeIn() with fadeOut() },
                ) {
                    when (val dest = this.currentHostEntry.destination) {
                        is RootScreenDestination -> {
                            RootScreen(
                                currentDestination = dest,
                                bottomNavItems = listOf(Destination.Home, Destination.Settings),
                                onNavClick = { navController.replaceLast(it) }
                            ) {
                                when (dest) {
                                    Destination.Home -> {} // TODO: Implement home screen.
                                    Destination.Settings -> {} // TODO: Implement settings screen.
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}