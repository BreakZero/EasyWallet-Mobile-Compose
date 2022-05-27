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
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.dappGraph(navController: NavController) {
    composable(
        DAppRouter.ROUTER_DETAIL + "?url={url}&chain={chain}&rpc={rpc}",
        arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            },
            navArgument("chain") {
                type = NavType.StringType
            },
            navArgument("rpc") {
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
        val chain = it.arguments?.getString("chain")!!.toIntOrNull() ?: 1
        val rpc = it.arguments?.getString("rpc")!!
        DAppWebViewScreen(
            url = url,
            chain = chain,
            rpc = rpc,
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
}