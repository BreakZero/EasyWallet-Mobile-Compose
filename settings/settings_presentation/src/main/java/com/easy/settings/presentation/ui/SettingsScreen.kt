package com.easy.settings.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.settings.presentation.components.MenuBlockView
import com.easy.settings.presentation.model.MenuItem
import com.easy.core.ext.toast
import com.easy.core.ui.R
import com.easy.core.ui.components.EasyAppBar
import com.easy.settings.presentation.SettingsRouter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    title: String,
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val context = LocalContext.current
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = null) {
        systemUIController.setStatusBarColor(color = Color.White, darkIcons = true)
    }
    val settingsState = settingsViewModel.settingsState
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                title = title,
                backgroundColor = Color.White
            ) {
                navigateUp()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(id = R.mipmap.avatar_generic_1),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(56.dp)
            )
            Text(
                text = "Wallet Name",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Security", menus = listOf(
                    MenuItem(
                        title = "Protect Your Wallet",
                        subTitle = "Passcode, Biometrics and 2FA"
                    ),
                    MenuItem(title = "Recovery Phrase", subTitle = "Wallet Name")
                )
            ) {
                val msg = when (it) {
                    0 -> "protect"
                    else -> "recovery"
                }
            }
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Account", menus = listOf(
                    MenuItem(title = "Display Currency", endValue = settingsState.displayCurrency),
                    MenuItem(title = "Network Settings", endValue = settingsState.currentNetwork)
                )
            ) {
                when(it) {
                    0 -> {
                        onNavigateTo.invoke(Navigator(SettingsRouter.SETTINGS_CURRENCY))
                    }
                    1 -> {
                        onNavigateTo.invoke(Navigator(SettingsRouter.SETTINGS_CHAINS))
                    }
                    else -> Unit
                }
            }
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Support", menus = listOf(
                    MenuItem(title = "Help Center"),
                    MenuItem(title = "New to DeFi"),
                    MenuItem(title = "Join Community"),
                    MenuItem(title = "Give Feedback")
                )
            ) {

            }
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "About EasyWallet", menus = listOf(
                    MenuItem(title = "Version", endValue = versionName, showIcon = false),
                    MenuItem(title = "Terms of Service"),
                    MenuItem(title = "Privacy Notice"),
                    MenuItem(title = "Visit our website")
                )
            ) {
                onNavigateTo.invoke(Navigator(SettingsRouter.SETTINGS_WEB) {
                    parameter {
                        "url" to "https://breakzero.github.io"
                    }
                })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

