package dev.ushiekane.xmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.NavBackHandler
import dev.olshevski.navigation.reimagined.rememberNavController
import dev.ushiekane.xmanager.ui.navigation.Destination
import dev.ushiekane.xmanager.ui.theme.XManagerTheme

class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XManagerTheme {
                val navController = rememberNavController<Destination>(startDestination = Destination.Home)

                NavBackHandler(navController)

                AnimatedNavHost(
                    controller = navController,
                ) { destination ->
                    when (destination) {
                        Destination.Home -> {} // TODO: Implement home screen.
                        Destination.Settings -> {} // TODO: Implement Settings screen.
                    }
                }
            }
        }
    }
}