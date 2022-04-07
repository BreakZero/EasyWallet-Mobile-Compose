
package com.easy.assets.presentation.assets

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.presentation.assets.components.CollapsableToolbar
import com.easy.core.ui.components.EasyActionBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class, ExperimentalAnimationApi::class)
@Composable
fun WalletPagerScreen(
    viewModel: WalletAssetViewModel = hiltViewModel(),
    onNavigateTo: (MainUIEvent) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AssetUIEvent.OnItemClick -> onNavigateTo.invoke(MainUIEvent.OnItemClicked(event.assetInfo))
                is AssetUIEvent.OnReceiveClick -> onNavigateTo.invoke(MainUIEvent.OnReceiveClick)
                is AssetUIEvent.OnSendClick -> onNavigateTo.invoke(MainUIEvent.OnSendClick)
            }
        }
    }
    val state = viewModel.assetState
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        EasyActionBar(
            navIcon = com.easy.core.ui.R.mipmap.avatar_generic_1,
            menuIcons = listOf(com.easy.core.ui.R.drawable.ic_scan_white),
            backgroundColor = Color(0xFF192B5E),
            tint = Color.White,
            onNavClick = {
                onNavigateTo(MainUIEvent.OnSettingsClick)
            },
            onMenuClick = {
                onNavigateTo(MainUIEvent.OnScanClick)
            }
        )
        CollapsableToolbar(
            onSend = {
                viewModel.onEvent(AssetEvent.OnSend)
            },
            onReceive = {
                viewModel.onEvent(AssetEvent.OnReceive)
            }
        ) {
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300, 300)) with
                            fadeOut(animationSpec = tween(300, 300))
                }) { state ->
                when {
                    state.tokenLists.isSuccess -> {
                        SwipeRefresh(
                            state = rememberSwipeRefreshState(state.isLoading),
                            swipeEnabled = it,
                            onRefresh = {
                                viewModel.onEvent(AssetEvent.OnRefresh)
                            }) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(
                                            topEnd = 24.dp,
                                            topStart = 24.dp
                                        )
                                    )
                            ) {
                                val assets = state.tokenLists.getOrElse { emptyList() }
                                items(items = assets) {
                                    AssetItemView(data = it) {
                                        viewModel.onEvent(AssetEvent.OnItemClick(it))
                                    }
                                }
                            }
                        }
                    }
                    state.tokenLists.isFailure -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topEnd = 24.dp,
                                        topStart = 24.dp
                                    )
                                )
                                .clickable {
                                    viewModel.onEvent(AssetEvent.OnRefresh)
                                }, contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = "something went wrong",
                                modifier = Modifier.padding(top = 100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetItemView(
    data: AssetInfo,
    onItemClick: (AssetInfo) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(56.dp)
            .clickable {
                onItemClick.invoke(data)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(40.dp),
                    painter = rememberImagePainter(
                        data = data.icon,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
                Text(
                    text = data.symbol,
                    modifier = Modifier.padding(start = 8.dp)
                )
                if (!data.tag.isNullOrEmpty()) {
                    Text(
                        text = data.tag!!,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF4FC3F7)
                            )
                            .padding(
                                start = 4.dp,
                                end = 4.dp,
                                top = 2.dp,
                                bottom = 2.dp
                            ),
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(text = "${data.balance} ${data.symbol}")
        }
    }
}