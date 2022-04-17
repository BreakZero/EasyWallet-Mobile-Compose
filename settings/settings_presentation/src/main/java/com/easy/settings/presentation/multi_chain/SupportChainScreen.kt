package com.easy.settings.presentation.multi_chain

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.EasyAppBar

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
        modifier = Modifier,
        topBar = {
            EasyAppBar(
                title = "Select Chain",
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