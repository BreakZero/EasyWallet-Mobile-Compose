package com.easy.assets.presentation.detail

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.easy.assets.domain.model.Transaction
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.TimeUtils
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ext.byDecimal
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors

@Composable
fun assetDetailViewModel(
    slug: String
): AssetDetailViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).assetDetailViewModelFactory()

    return viewModel(
        factory = AssetDetailViewModel.provideFactory(
            factory,
            slug
        )
    )
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalCoilApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun AssetDetailScreen(
    slug: String,
    viewModel: AssetDetailViewModel = assetDetailViewModel(slug),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val systemUIController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val statusColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(key1 = true) {
        systemUIController.setStatusBarColor(statusColor, useDarkIcons)
    }
    val state = viewModel.state
    val lazyPagingItems = viewModel.state.pager?.flow?.collectAsLazyPagingItems()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            EasyAppBar(
                title = state.assetInfo?.symbol
            ) {
                navigateUp.invoke()
            }
        }
    ) {
        val isRefreshing = lazyPagingItems?.loadState == null ||
                lazyPagingItems.loadState.refresh == LoadState.Loading
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            SwipeRefresh(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    viewModel.onEvent(AssetDetailEvent.OnRefresh)
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
                            Column(modifier = Modifier.weight(1f)) {
                                Row() {
                                    Text(
                                        text = state.balance.getOrNull() ?: "--",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.alignByBaseline()
                                    )
                                    Text(
                                        textAlign = TextAlign.Justify,
                                        text = state.assetInfo?.symbol.orEmpty(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .alignByBaseline()
                                    )
                                }
                                Text(
                                    text = "$ 3.84",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            AsyncImage(
                                modifier = Modifier.size(40.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.assetInfo?.icon)
                                    .transformations(CircleCropTransformation())
                                    .build(),
                                contentDescription = null
                            )
                        }
                    }
                    stickyHeader {
                        Text(
                            text = "Tx history",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .height(24.dp)
                                .fillMaxWidth()
                                .background(Color(0x22888888))
                                .padding(start = 16.dp),
                            color = Color.Gray
                        )
                    }
                    lazyPagingItems?.let {

                        items(lazyPagingItems) {
                            it?.let {
                                TransactionItemView(
                                    transactionInfo = it,
                                    state.assetInfo?.decimal ?: 0
                                ) {

                                }
                                Divider(
                                    modifier = Modifier
                                        .height(0.2.dp)
                                        .padding(start = 16.dp)
                                )
                            }
                        }
                        if (lazyPagingItems.loadState.append == LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                    /*state.transactions.getOrNull()?.let {
                        items(it) {
                            TransactionItemView(
                                transactionInfo = it,
                                state.assetInfo?.decimal ?: 0
                            ) {

                            }
                            Divider(
                                modifier = Modifier
                                    .height(0.2.dp)
                                    .padding(start = 16.dp)
                            )
                        }
                    } ?: item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "empty...",
                                modifier = Modifier
                                    .clickable {
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }*/
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
                        onNavigateTo.invoke(
                            Navigator(router = AssetRouter.ASSET_RECEIVE) {
                                parameter {
                                    "address" to viewModel.address()
                                }
                            })
                    },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Receive", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.mockSign()
                        onNavigateTo.invoke(Navigator(router = AssetRouter.SEND_FIRST) {
                            parameter {
                                "slug" to viewModel.state.assetInfo?.slug
                            }
                        })
                    }, modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Send", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TransactionItemView(transactionInfo: Transaction, decimal: Int, click: (Transaction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable { click.invoke(transactionInfo) }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (transactionInfo.isReceive) Icons.Outlined.ArrowCircleDown
                else Icons.Outlined.ArrowCircleUp,
                tint = if (transactionInfo.isReceive) Color.Green else Color.Red,
                contentDescription = null,
            )
            Text(
                text = if (transactionInfo.isReceive) "Received" else "Send",
                color = if (transactionInfo.isReceive) Color.Green else Color.Red
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                textAlign = TextAlign.End,
                text = "${if (transactionInfo.isReceive) "+" else "-"} ${
                    transactionInfo.value.toBigInteger().byDecimal(decimal, 8)
                }",
                color = if (transactionInfo.isReceive) Color.Green else Color.Red,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                textAlign = TextAlign.End,
                text = TimeUtils.timestampToString(transactionInfo.timeStamp.toLong())
            )
        }
    }
}