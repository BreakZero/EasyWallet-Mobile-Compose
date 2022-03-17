package com.easy.wallet.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.assets.presentation.assets.MainUIEvent
import com.easy.assets.presentation.assets.WalletPagerScreen
import com.easy.assets.presentation.assets.components.BottomMenuItem
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ui.GlobalRouter
import com.easy.core.ui.R
import com.easy.core.ui.common.QRCodeGenerate
import com.easy.dapp.presentation.list.DAppPagerScreen
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

val menus = listOf(
    BottomMenuItem(
        icon = R.drawable.ic_bottom_menu_wallet,
        label = "Wallet"
    ),
    BottomMenuItem(
        icon = R.drawable.ic_bottom_menu_dapps,
        label = "DApps"
    )
)

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateTo: (Navigator) -> Unit
) {
    val pageState = rememberPagerState()
    val tabIndex = pageState.currentPage
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    LaunchedEffect(key1 = null) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MainUIEvent.OnScanClick -> onNavigateTo.invoke(Navigator(router = GlobalRouter.GLOBAL_SCAN))
                is MainUIEvent.OnItemClicked -> {
                    onNavigateTo.invoke(
                        Navigator(router = AssetRouter.ASSET_DETAIL) {
                            parameter {
                                "slug" to event.assetInfo.slug
                            }
                        }
                    )
                }
                is MainUIEvent.OnSettingsClick -> onNavigateTo.invoke(Navigator(router = AssetRouter.SETTINGS))
                is MainUIEvent.OnReceiveClick -> bottomSheetState.show()
                is MainUIEvent.OnSendClick -> Unit
            }
        }
    }

    val systemUIController = rememberSystemUiController()
    val isFirstPager = pageState.currentPage == 0
    LaunchedEffect(key1 = pageState.currentPage) {
        systemUIController.setStatusBarColor(
            if (isFirstPager) Color(0xFF192B5E)
            else Color.White, darkIcons = !isFirstPager
        )
    }
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                QRCodeGenerate.genQRCode("Hello world")?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(), contentDescription = "",
                            alignment = Alignment.Center
                        )
                        Text(
                            text = "Hello world",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                count = menus.size,
                state = pageState
            ) { page ->
                when (page) {
                    0 -> {
                        WalletPagerScreen(onNavigateTo = {
                            viewModel.onUiEvent(it)
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
            Divider()
            TabRow(
                backgroundColor = Color.White,
                selectedTabIndex = tabIndex,
                modifier = Modifier.height(56.dp),
                indicator = {}) {
                menus.forEachIndexed { index, bottomMenuItem ->
                    Tab(
                        selected = index == tabIndex,
                        selectedContentColor = Color(0xFF1199FA),
                        unselectedContentColor = Color(0xFF808C99),
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
}