package com.easy.dapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.easy.core.ui.components.EasyActionBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun DAppPagerScreen(
    viewModel: DAppViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
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
    }
    if (viewModel.dAppState.isLoading) {
        CircularProgressIndicator()
    } else {
        viewModel.dAppState.dApps.getOrNull()?.let {
            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                items(it) {
                    Column() {
                        Image(painter = rememberImagePainter(
                            data = it.icon,
                            builder = {
                                transformations(CircleCropTransformation())
                            }
                        ), contentDescription = null)
                        Text(text = it.name)
                    }
                }
            }
        } ?: {

        }
    }

}