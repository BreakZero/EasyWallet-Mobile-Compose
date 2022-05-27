package com.easy.core.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.easy.core.ui.screens.ScanScreen
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.globalGraph(navController: NavController) {
    composable(
        GlobalRouter.GLOBAL_SCAN,
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
        ScanScreen { qrCodeContent ->
            navController.previousBackStackEntry?.savedStateHandle?.let {
                it.set("QR_CODE_CONTENT", qrCodeContent)
            }
            navController.navigateUp()
        }
    }
}