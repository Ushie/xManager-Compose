package dev.ushiekane.xmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import dev.olshevski.navigation.reimagined.*
import dev.ushiekane.xmanager.ui.navigation.Destination
import dev.ushiekane.xmanager.ui.navigation.RootScreenDestination
import dev.ushiekane.xmanager.ui.screen.RootScreen
import dev.ushiekane.xmanager.ui.theme.XManagerTheme

class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XManagerTheme(
                darkTheme = true, // TODO: Add preferences.
                dynamicColor = true
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
                                    Destination.Settings -> {} // TODO: Implement Settings screen.
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}