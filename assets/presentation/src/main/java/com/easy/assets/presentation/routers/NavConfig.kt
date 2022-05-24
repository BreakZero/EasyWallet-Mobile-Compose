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
import com.easy.assets.presentation.send.SendingScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.assetsGraph(navController: NavController) {
    navigation(startDestination = AssetRouter.ASSET_DETAIL, route = AssetRouter.ASSET_ASSETS) {
        composable(
            AssetRouter.ASSET_DETAIL + "/{slug}",
            arguments = listOf(
                navArgument("slug") {
                    type = NavType.StringType
                }
            ),
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
            val slug = it.arguments?.getString("slug")!!
            AssetDetailScreen(
                slug = slug,
                navigateUp = {
                    navController.navigateUp()
                }
            ) { nav ->
                navController.navigate(nav.router())
            }
        }
        composable(
            route = AssetRouter.ASSET_RECEIVE + "/{address}",
            arguments = listOf(
                navArgument("address") {
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
            val address = it.arguments?.getString("address")!!
            ReceiveScreen(address = address) {
                navController.navigateUp()
            }
        }
        composable(
            route = AssetRouter.SEND_FIRST + "/{slug}",
            arguments = listOf(
                navArgument("slug") {
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
            val slug = it.arguments?.getString("slug")!!
            SendingScreen(
                slug = slug,
                savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                navigateUp = {
                    navController.navigateUp()
                }
            ) {
                navController.navigate(it.router())
            }
        }
    }
}