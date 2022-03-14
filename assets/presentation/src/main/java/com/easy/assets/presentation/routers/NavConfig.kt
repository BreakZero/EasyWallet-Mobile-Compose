package com.easy.assets.presentation.routers

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.assets.presentation.detail.AssetDetailScreen
import com.easy.assets.presentation.receive.ReceiveScreen
import com.easy.assets.presentation.send.NormalSendAddressScreen
import com.easy.assets.presentation.send.NormalSendAmountScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.assetsGraph(navController: NavController) {
    navigation(startDestination = AssetRouter.ASSET_DETAIL, route = AssetRouter.ASSET_ASSETS) {
        composable(
            AssetRouter.ASSET_DETAIL + "/{slug}/{symbol}/{contract}",
            arguments = listOf(
                navArgument("symbol") {
                    type = NavType.StringType
                },
                navArgument("slug") {
                    type = NavType.StringType
                },
                navArgument("contract") {
                    type = NavType.StringType
                }),
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }) {
            val symbol = it.arguments?.getString("symbol")!!
            val slug = it.arguments?.getString("slug")!!
            val contract = it.arguments?.getString("contract")!!
            AssetDetailScreen(
                contractAddress = contract,
                symbol = symbol,
                slug = slug,
                navigateUp = {
                    navController.navigateUp()
                }
            ) { nav ->
                navController.navigate(nav.router())
            }
        }
        composable(route = AssetRouter.ASSET_RECEIVE,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }) {
            ReceiveScreen(address = "0x6b175474e89094c44da98b954eedeac495271d0f") {
                navController.navigateUp()
            }
        }
        composable(route = AssetRouter.SEND_FIRST,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }) {
            NormalSendAddressScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            ) {
                navController.navigate(it.router())
            }
        }
        composable(route = AssetRouter.SEND_SECOND,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }) {
            NormalSendAmountScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            ) {
                navController.navigateUp()
            }
        }
    }
}