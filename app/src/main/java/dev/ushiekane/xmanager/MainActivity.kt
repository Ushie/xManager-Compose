package dev.ushiekane.xmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xinto.taxi.Taxi
import com.xinto.taxi.rememberBackstackNavigator
import dev.ushiekane.xmanager.ui.navigation.AppDestination
import dev.ushiekane.xmanager.ui.screen.HomeScreen
import dev.ushiekane.xmanager.ui.theme.XManagerTheme
import dev.ushiekane.xmanager.ui.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            XManagerTheme {
                val navigator = rememberBackstackNavigator<AppDestination>(AppDestination.Home)

                BackHandler {
                    navigator.pop()
                }

                Taxi(
                    modifier = Modifier.fillMaxSize(),
                    navigator = navigator,
                    transitionSpec = { fadeIn() with fadeOut() }
                ) { destination ->
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