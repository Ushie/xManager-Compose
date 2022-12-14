package dev.ushiekane.xmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.rememberNavController
import dev.ushiekane.xmanager.ui.navigation.AppDestination
import dev.ushiekane.xmanager.ui.screen.HomeScreen
import dev.ushiekane.xmanager.ui.theme.XManagerTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            XManagerTheme {
                val navigator = rememberNavController(AppDestination.Home)

                NavBackHandler(navigator)

                AnimatedNavHost(controller = navigator) { destination ->
                    @Suppress("USELESS_IS_CHECK") // Settings page isn't added yet
                    when (destination) {
                        is AppDestination.Home -> HomeScreen(
                            onClickSettings = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}