package com.easy.assets.presentation.detail

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.assets.domain.model.Transaction
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.TimeUtils
import com.easy.core.common.Navigator
import com.easy.core.common.navigator
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors

@Composable
fun assetDetailViewModel(
    tokenParam: AssetBundle
): AssetDetailViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).assetDetailViewModelFactory()

    return viewModel(
        factory = AssetDetailViewModel.provideFactory(
            factory,
            tokenParam
        )
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun AssetDetailScreen(
    contractAddress: String,
    symbol: String,
    slug: String,
    viewModel: AssetDetailViewModel = assetDetailViewModel(
        AssetBundle(symbol = symbol, contractAddress = contractAddress, slug = slug)
    ),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = true) {
        systemUIController.setStatusBarColor(Color.White, true)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                backgroundColor = Color.White,
                title = symbol
            ) {
                navigateUp.invoke()
            }
        }
    ) {
        val state = viewModel.state
        Column(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
                onRefresh = {

                }) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column() {
                                Row {
                                    Text(
                                        text = state.balance.getOrNull() ?: "--",
                                        style = MaterialTheme.typography.h4,
                                        modifier = Modifier.alignByBaseline()
                                    )
                                    Text(
                                        textAlign = TextAlign.Justify,
                                        text = state.symbol,
                                        style = MaterialTheme.typography.body2,
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .alignByBaseline()
                                    )
                                }
                                Text(
                                    text = "$ 3.84",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.Gray
                                )
                            }
                            Image(
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.FillWidth,
                                painter = rememberImagePainter(
                                    data = state.icon,
                                    builder = {
                                        transformations(CircleCropTransformation())
                                    }
                                ),
                                contentDescription = null
                            )
                        }
                    }
                    stickyHeader {
                        Text(
                            text = "Tx history",
                            modifier = Modifier
                                .height(24.dp)
                                .fillMaxWidth()
                                .background(Color(0x22888888))
                                .padding(start = 32.dp),
                            color = Color.Gray
                        )
                    }
                    state.result.getOrNull()?.let {
                        items(it) {
                            TransactionItemView(transactionInfo = it) {

                            }
                            Divider(
                                modifier = Modifier
                                    .height(0.2.dp)
                                    .padding(start = 16.dp)
                            )
                        }
                    } ?: item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(text = "empty")
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        onNavigateTo.invoke(navigator(AssetRouter.ASSET_RECEIVE) {
                            add {
                                "String" to viewModel.address()
                            }
                        })
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Text(text = "Receive", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        onNavigateTo.invoke(navigator(router = "send-flow-1") {
                        })
                    }, modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                ) {
                    Text(text = "Send", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TransactionItemView(transactionInfo: Transaction, click: (Transaction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable { click.invoke(transactionInfo) }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier) {
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = null
            )
            Text(text = "Received")
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(text = transactionInfo.value, overflow = TextOverflow.Ellipsis)
            Text(text = TimeUtils.timestampToString(transactionInfo.timeStamp.toLong()))
        }
    }
}