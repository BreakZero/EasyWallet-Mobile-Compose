package com.easy.settings.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.easy.settings.presentation.multi_chain.SupportChainScreen
import com.easy.settings.presentation.ui.SettingsScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsGraph(navController: NavController) {
    navigation(startDestination = SettingsRouter.SETTINGS, route = SettingsRouter.SETTINGS_INDEX) {
        composable(
            SettingsRouter.SETTINGS,
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
            SettingsScreen("Settings",
                navigateUp = {
                    navController.navigateUp()
                },
                onNavigateTo = {
                    navController.navigate(it.router)
                }
            )
        }
        composable(SettingsRouter.SETTINGS_CHAINS,
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
            SupportChainScreen(onNavigateUp = {
                navController.navigateUp()
            })
        }
    }
}