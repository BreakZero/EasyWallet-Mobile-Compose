package com.easy.intro

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation(
        startDestination = IntroRouter.ROUTER_INTRO,
        route = IntroRouter.ROUTER_INTRO_INDEX
    ) {
        composable(
            IntroRouter.ROUTER_INTRO,
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
            WalletCreateScreen(onNavigate = {
                navController.navigate(it.router())
            }, onNavigateUp = {
                navController.navigateUp()
            })
        }
        composable(
            IntroRouter.ROUTER_WALLET_IMPORT,
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
            WalletImportScreen(
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
            PasscodeScreen {
                navController.previousBackStackEntry?.savedStateHandle?.set("passcode", it)
                navController.navigateUp()
            }
        }
    }
}