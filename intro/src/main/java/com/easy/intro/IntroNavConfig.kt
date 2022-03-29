package com.easy.intro

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.ScaffoldState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.easy.intro.intro.IntroMainScreen
import com.easy.intro.model.IntroInfo
import com.easy.intro.passcode.PasscodeScreen
import com.easy.intro.walletcreate.WalletCreateScreen
import com.easy.intro.walletimport.WalletImportScreen
import com.easy.intro.walletprotect.WalletProtectScreen
import com.easy.intro.walletsetup.WalletSetupScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import logcat.logcat

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.introGraph(navController: NavController, scaffoldState: ScaffoldState) {
    navigation(
        startDestination = IntroRouter.ROUTER_INTRO,
        route = IntroRouter.ROUTER_INTRO_INDEX
    ) {
        composable(
            IntroRouter.ROUTER_INTRO,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) {
            IntroMainScreen(
                title = stringResource(id = com.easy.core.ui.R.string.app_name), intros = listOf<IntroInfo>(
                    IntroInfo(
                        "Welcome to EasyWallet",
                        "Trusted by millions, MetaMask is a secure wallet making the world of web3",
                        R.mipmap.banner_welcome
                    ),
                    IntroInfo(
                        "Welcome to EasyWallet",
                        "Trusted by millions, MetaMask is a secure wallet making the world of web3",
                        R.mipmap.banner_swap_tutorial_dialog
                    ), IntroInfo(
                        "Welcome to EasyWallet",
                        "Trusted by millions, MetaMask is a secure wallet making the world of web3",
                        R.mipmap.banner_swap_cross_chain
                    )
                )
            ) {
                navController.navigate(route = IntroRouter.ROUTER_WALLET_SETUP)
            }
        }
        composable(
            IntroRouter.ROUTER_WALLET_SETUP,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) {
            WalletSetupScreen(title = stringResource(id = com.easy.core.ui.R.string.app_name)) {
                when (it) {
                    "back" -> navController.navigateUp()
                    "to wallet create" -> navController.navigate(IntroRouter.ROUTER_WALLET_CREATE)
                    "import a wallet" -> navController.navigate(IntroRouter.ROUTER_WALLET_IMPORT)
                }
            }
        }
        composable(
            IntroRouter.ROUTER_WALLET_CREATE,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) {
            WalletCreateScreen(onNavigate = {
                navController.navigate(it.router())
            }, onNavigateUp = {
                navController.navigateUp()
            })
        }
        composable(
            IntroRouter.ROUTER_WALLET_IMPORT,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) {
            WalletImportScreen(
                scaffoldState = scaffoldState,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToAssets = {
                    navController.navigate(route = IntroRouter.ROUTER_MAIN) {
                        popUpTo(IntroRouter.ROUTER_INTRO) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            IntroRouter.ROUTER_WALLET_PROTECT,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) { it->
            val passcode = it.savedStateHandle.get<String>("passcode")
            WalletProtectScreen(
                passcode = passcode,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigate = {
                    if (it.router == IntroRouter.ROUTER_MAIN) {
                        navController.navigate(it.router()) {
                            popUpTo(IntroRouter.ROUTER_INTRO) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(it.router())
                    }
                }
            )
        }
        composable(
            IntroRouter.ROUTER_PASSCODE,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
            }) {
            PasscodeScreen {
                navController.previousBackStackEntry?.savedStateHandle?.set("passcode", it)
                navController.navigateUp()
            }
        }
    }
}