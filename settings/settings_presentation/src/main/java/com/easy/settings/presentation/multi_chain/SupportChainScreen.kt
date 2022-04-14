package com.easy.settings.presentation.multi_chain

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SupportChainScreen(
    supportChainViewModel: SupportChainViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val state = supportChainViewModel.state
    LaunchedEffect(key1 = supportChainViewModel) {
        supportChainViewModel.uiEvent.collect {
            when (it) {
                ChainUIEvent.ActionDone -> {
                    onNavigateUp()
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            EasyAppBar(
                navIcon = Icons.Filled.ArrowBack,
                title = "Select Chain",
                backgroundColor = Color.White,
                actions = {
                    IconButton(onClick = {
                        supportChainViewModel.onEvent(ChainEvent.OnDone)
                    }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    }
                }) {
                onNavigateUp()
            }
        }
    ) {
        LazyColumn {
            items(state.supportNetworks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = it == state.selectedOne, onClick = {
                        supportChainViewModel.onEvent(ChainEvent.OnSelected(it))
                    })
                    Text(text = it.name)
                }
            }
        }
    }
}