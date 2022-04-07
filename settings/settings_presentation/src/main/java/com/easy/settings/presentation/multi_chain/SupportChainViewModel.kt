package com.easy.settings.presentation.multi_chain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.enums.Chain
import com.easy.core.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SupportChainViewModel @Inject constructor(
    private val appSettings: DataStore<AppSettings>
) : ViewModel() {
    var state: SupportChainState by mutableStateOf(
        SupportChainState(
            supportChains = listOf(
                Chain.ETHEREUM,
                Chain.MATIC
            )
        )
    )
    private val _uiEvent = Channel<ChainUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.data.collect {
                withContext(Dispatchers.Main) {
                    state = state.copy(checkId = it.chain.id)
                }
            }
        }
    }

    fun onEvent(event: ChainEvent) {
        when (event) {
            ChainEvent.OnDone -> {
                viewModelScope.launch {
                    updateChain()
                    _uiEvent.send(ChainUIEvent.ActionDone)
                }
            }
            is ChainEvent.OnSelected -> {
                state = state.copy(checkId = event.selectedId)
            }
        }
    }

    private suspend fun updateChain() {
        appSettings.updateData {
            it.copy(chain = state.selected())
        }
    }
}
