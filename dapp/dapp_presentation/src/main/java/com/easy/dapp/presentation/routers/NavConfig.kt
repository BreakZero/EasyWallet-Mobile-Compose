package com.easy.dapp.presentation.routers

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.easy.dapp.presentation.detail.DAppWebViewScreen
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.dappGraph(navController: NavController) {
    composable(
        DAppRouter.ROUTER_DETAIL + "?url={url}",
        arguments = listOf(
            navArgument("url") {
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
        val url = it.arguments?.getString("url")!!
        DAppWebViewScreen(
            url = url,
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
}