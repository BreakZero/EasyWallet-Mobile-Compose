package com.easy.settings.presentation.currencies

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.easy.core.ui.components.EasyAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val state = currencyViewModel.currencyState
    LaunchedEffect(key1 = currencyViewModel) {
        currencyViewModel.uiEvent.collect {
            when (it) {
                CurrencyUIEvent.ActionDone -> {
                    onNavigateUp()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            EasyAppBar(
                title = "Select Currency",
                actions = {
                    IconButton(onClick = {
                        currencyViewModel.onEvent(CurrencyEvent.OnDone)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            ) {
                onNavigateUp()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
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
