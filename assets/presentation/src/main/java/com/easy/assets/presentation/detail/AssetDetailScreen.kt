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
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleUp
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
import com.easy.assets.domain.model.Transaction
import com.easy.assets.presentation.di.ViewModelFactoryProvider
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.TimeUtils
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ext.byDecimal
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.EntryPointAccessors
import logcat.logcat

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

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun AssetDetailScreen(
    slug: String,
    viewModel: AssetDetailViewModel = assetDetailViewModel(slug),
    navigateUp: () -> Unit,
    onNavigateTo: (Navigator) -> Unit
) {
    val systemUIController = rememberSystemUiController()
    LaunchedEffect(key1 = true) {
        systemUIController.setStatusBarColor(Color.White, true)
    }
    val state = viewModel.state
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                backgroundColor = Color.White,
                title = state.assetInfo?.symbol
            ) {
                navigateUp.invoke()
            }
        }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            SwipeRefresh(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
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
                                        style = MaterialTheme.typography.h4,
                                        modifier = Modifier.alignByBaseline()
                                    )
                                    Text(
                                        textAlign = TextAlign.Justify,
                                        text = state.assetInfo?.symbol.orEmpty(),
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
                                    data = state.assetInfo?.icon,
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
                    state.transactions.getOrNull()?.let {
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
                                        logcat { "re-try" }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
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
                        onNavigateTo.invoke(
                            Navigator(router = AssetRouter.ASSET_RECEIVE) {
                                parameter {
                                    "address" to viewModel.address()
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
                        viewModel.mockSign()
                        onNavigateTo.invoke(Navigator(router = AssetRouter.SEND_FIRST) {
                            parameter {
                                "slug" to viewModel.state.assetInfo?.slug
                            }
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
        Row(modifier = Modifier) {
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