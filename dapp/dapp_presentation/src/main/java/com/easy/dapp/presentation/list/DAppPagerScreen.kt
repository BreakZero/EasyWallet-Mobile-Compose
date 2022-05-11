package com.easy.dapp.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.core.common.Navigator
import com.easy.core.common.parameter
import com.easy.core.ui.components.EasyActionBar
import com.easy.dapp.presentation.routers.DAppRouter

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun DAppPagerScreen(
    viewModel: DAppViewModel = hiltViewModel(),
    onNavigateTo: (Navigator) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EasyActionBar(
            navIcon = com.easy.core.ui.R.drawable.ic_bottom_menu_dapps,
            menuIcons = listOf(com.easy.core.ui.R.drawable.ic_scan_white),
            backgroundColor = Color.White,
            tint = Color.Black,
            onNavClick = {

            },
            onMenuClick = {

            }
        )
        if (viewModel.dAppState.isLoading) {
            CircularProgressIndicator()
        } else {
            viewModel.dAppState.dApps.getOrNull()?.let {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
                ) {
                    items(it) {
                        Column(
                            modifier = Modifier
                                .clickable {
                                    onNavigateTo.invoke(Navigator(DAppRouter.ROUTER_DETAIL) {
                                        parameter {
                                            "url" to it.url
                                        }
                                    })
                                }
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(modifier = Modifier.size(48.dp), painter = rememberImagePainter(
                                data = it.icon,
                                builder = {
                                    transformations(CircleCropTransformation())
                                }
                            ), contentDescription = null)
                            Text(text = it.name, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    }
}
