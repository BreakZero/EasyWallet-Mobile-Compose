
package com.easy.assets.presentation.assets

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.presentation.assets.components.CollapsableToolbar
import com.easy.core.common.Navigator
import com.easy.core.common.UiEvent
import com.easy.core.ui.components.EasyActionBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun WalletPagerScreen(
    viewModel: WalletAssetViewModel = hiltViewModel(),
    onNavigateTo: (Navigator) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> onNavigateTo.invoke(event.navigator)
                else -> Unit
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
            backgroundColor = MaterialTheme.colorScheme.primary,
            tint = MaterialTheme.colorScheme.onPrimary,
            onNavClick = {
                viewModel.onEvent(AssetEvent.SettingsClicked)
            },
            onMenuClick = {
                viewModel.onEvent(AssetEvent.ScanClicked)
            }
        )
        CollapsableToolbar {
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
                                viewModel.onEvent(AssetEvent.SwipeToRefresh)
                            }) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(
                                        RoundedCornerShape(
                                            topEnd = 24.dp,
                                            topStart = 24.dp
                                        )
                                    )
                                    .background(
                                        color = MaterialTheme.colorScheme.background
                                    )
                                    .padding(top = 12.dp)

                            ) {
                                val assets = state.tokenLists.getOrElse { emptyList() }
                                items(items = assets) {
                                    AssetItemView(data = it) {
                                        viewModel.onEvent(AssetEvent.ItemClicked(it))
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
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(
                                        topEnd = 24.dp,
                                        topStart = 24.dp
                                    )
                                )
                                .clickable {
                                    viewModel.onEvent(AssetEvent.SwipeToRefresh)
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

@OptIn(ExperimentalMaterial3Api::class)
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
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(40.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.icon)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )
                Text(
                    text = data.symbol,
                    modifier = Modifier.padding(start = 8.dp)
                )
                data.tag?.let {
                    Text(
                        text = it.name,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
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