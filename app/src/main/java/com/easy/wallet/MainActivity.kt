package com.easy.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.easy.assets.presentation.routers.assetsGraph
import com.easy.core.ui.globalGraph
import com.easy.dapp.presentation.routers.DAppRouter
import com.easy.dapp.presentation.routers.dappGraph
import com.easy.intro.IntroRouter
import com.easy.intro.introGraph
import com.easy.settings.settingsGraph
import com.easy.wallet.ui.main.MainScreen
import com.easy.wallet.ui.splash.SplashScreen
import com.easy.wallet.ui.theme.EasyTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EasyTheme {
                ProvideWindowInsets {
                    val systemUIController = rememberSystemUiController()
                    SideEffect {
                        systemUIController.setStatusBarColor(Color.Transparent, darkIcons = true)
                    }
                    val navController = rememberAnimatedNavController()
                    val scaffoldState = rememberScaffoldState()
                    // A surface container using the 'background' color from the theme
                    Scaffold(
                        scaffoldState = scaffoldState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = "splash-screen",
                            modifier = Modifier.padding(it)
                        ) {
                            composable(
                                "splash-screen",
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { 1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                exitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = { -1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                popEnterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { -1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = { 1000 },
                                        animationSpec = tween(700)
                                    )
                                }) {
                                SplashScreen { created ->
                                    navController.navigate(if (created) "main-screen" else IntroRouter.ROUTER_INTRO_INDEX) {
                                        popUpTo("splash-screen") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            composable(
                                "main-screen",
                                enterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { 1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                exitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = { -1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                popEnterTransition = {
                                    slideInHorizontally(
                                        initialOffsetX = { -1000 },
                                        animationSpec = tween(700)
                                    )
                                },
                                popExitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = { 1000 },
                                        animationSpec = tween(700)
                                    )
                                }) {
                                MainScreen {
                                    if (it.router == DAppRouter.ROUTER_DETAIL) {
                                        navController.navigate(it.routerWithParameter())
                                    } else {
                                        navController.navigate(it.router())
                                    }
                                }
                            }
                            globalGraph(navController)
                            introGraph(navController, scaffoldState)
                            assetsGraph(navController)
                            dappGraph(navController)
                            settingsGraph(navController)
                        }
                    }
                }
            }
        }
    }
}
