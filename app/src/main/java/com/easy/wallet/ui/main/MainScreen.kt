package com.easy.wallet.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.assets.presentation.assets.WalletPagerScreen
import com.easy.assets.presentation.assets.components.BottomMenuItem
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ui.GlobalRouter
import com.easy.core.ui.R
import com.easy.dapp.presentation.list.DAppPagerScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

val menus = listOf(
    BottomMenuItem(
        icon = R.drawable.ic_bottom_menu_wallet, label = "Wallet"
    ), BottomMenuItem(
        icon = R.drawable.ic_bottom_menu_dapps, label = "DApps"
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    onNavigateTo: (Navigator) -> Unit
) {
    val pageState = rememberPagerState()
    val tabIndex = pageState.currentPage
    val scope = rememberCoroutineScope()

    val systemUIController = rememberSystemUiController()
    val useDartIcons = isSystemInDarkTheme()
    val statusColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(key1 = pageState.currentPage) {
        systemUIController.setStatusBarColor(
            statusColor, useDartIcons
        )
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), count = menus.size, state = pageState
        ) { page ->
            when (page) {
                0 -> {
                    WalletPagerScreen(onNavigateTo = {
                        onNavigateTo(it)
                    })
                }
                1 -> {
                    DAppPagerScreen {
                        onNavigateTo.invoke(it)
                    }
                }
                else -> Unit
            }
        }
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.height(56.dp),
            indicator = {}) {
            menus.forEachIndexed { index, bottomMenuItem ->
                Tab(selected = index == tabIndex,
                    selectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(index)
                        }
                    }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = bottomMenuItem.icon),
                            contentDescription = null
                        )
                        Text(text = bottomMenuItem.label)
                    }
                }
            }
        }
    }
}