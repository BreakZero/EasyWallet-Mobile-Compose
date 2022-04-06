package com.easy.settings.presentation.currencies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.EasyAppBar
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collect

@Composable
fun CurrencyScreen(
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val state = currencyViewModel.currencyState
    LaunchedEffect(key1 = currencyViewModel) {
        currencyViewModel.uiEvent.collect {
            when(it) {
                CurrencyUIEvent.ActionDone -> {
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
                title = "Select Currency",
                backgroundColor = Color.White,
                actions = {
                    IconButton(onClick = {
                        currencyViewModel.onEvent(CurrencyEvent.OnDone)
                    }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    }
                }) {
                onNavigateUp()
            }
        }
    ) {
        LazyColumn {
            items(state.supportList) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = it.currencyCode == state.selected.currencyCode, onClick = {
                        currencyViewModel.onEvent(CurrencyEvent.OnSelected(it))
                    })
                    Text(text = "${it.symbol} (${it.currencyCode})")
                }
            }
        }
    }
}