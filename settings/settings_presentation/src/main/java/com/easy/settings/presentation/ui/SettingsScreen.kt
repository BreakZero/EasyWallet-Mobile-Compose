package com.easy.settings.presentation.ui

import android.widget.Toast
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
import com.easy.settings.presentation.components.MenuBlockView
import com.easy.settings.presentation.model.MenuItem
import com.easy.core.ext.toast
import com.easy.core.ui.R
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingsScreen(
    title: String,
    navigateUp: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val context = LocalContext.current
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = null) {
        systemUIController.setStatusBarColor(color = Color.White, darkIcons = true)
    }

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
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
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
                context.toast(msg)
            }
            MenuBlockView(
                modifier = Modifier.fillMaxWidth(), header = "Account", menus = listOf(
                    MenuItem(title = "Display Currency", endValue = "$ USD"),
                    MenuItem(title = "Network Settings", endValue = "Mainnet")
                )
            ) {
                context.toast("click: $it")
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

            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
