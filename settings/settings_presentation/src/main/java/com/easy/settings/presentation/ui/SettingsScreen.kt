package com.easy.settings.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ui.R
import com.easy.core.ui.components.EasyAppBar
import com.easy.core.ui.spacing
import com.easy.settings.presentation.SettingsRouter
import com.easy.settings.presentation.components.MenuBlockView
import com.easy.settings.presentation.model.MenuItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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
    val useDarkIcons = !isSystemInDarkTheme()
    val statusColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(key1 = null) {
        systemUIController.setStatusBarColor(color = statusColor, darkIcons = useDarkIcons)
    }
    val settingsState = settingsViewModel.settingsState
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        EasyAppBar(
            title = title
        ) {
            navigateUp()
        }
    }) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    start = MaterialTheme.spacing.spaceMedium,
                    end = MaterialTheme.spacing.spaceMedium
                )
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
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Security", menus = listOf(
                    MenuItem(
                        title = "Protect Your Wallet", subTitle = "Passcode, Biometrics and 2FA"
                    ), MenuItem(title = "Recovery Phrase", subTitle = "Wallet Name")
                )
            ) {}
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Account", menus = listOf(
                    MenuItem(title = "Display Currency", endValue = settingsState.displayCurrency),
                    MenuItem(
                        title = "Network Settings",
                        endValue = settingsState.currentNetwork.label
                    )
                )
            ) {
                when (it) {
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

