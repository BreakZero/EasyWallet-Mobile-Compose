package com.easy.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
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
import com.easy.settings.presentation.settingsGraph
import com.easy.wallet.ui.main.MainScreen
import com.easy.wallet.ui.splash.SplashScreen
import com.easy.wallet.ui.theme.EasyTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
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
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding()
                    ) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = "splash-screen",
                            modifier = Modifier
                        ) {
                            composable(
                                "splash-screen",
                                enterTransition = {
                                    fadeIn(animationSpec = tween(500))
                                },
                                exitTransition = {
                                    fadeOut(animationSpec = tween(500))
                                },
                                popEnterTransition = {
                                    fadeIn(animationSpec = tween(500))
                                },
                                popExitTransition = {
                                    fadeOut(animationSpec = tween(500))
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
                                    fadeIn(animationSpec = tween(500))
                                },
                                exitTransition = {
                                    fadeOut(animationSpec = tween(500))
                                },
                                popEnterTransition = {
                                    fadeIn(animationSpec = tween(500))
                                },
                                popExitTransition = {
                                    fadeOut(animationSpec = tween(500))
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
                            introGraph(navController)
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
