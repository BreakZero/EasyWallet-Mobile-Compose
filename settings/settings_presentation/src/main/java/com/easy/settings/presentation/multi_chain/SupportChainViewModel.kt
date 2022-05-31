package com.easy.settings.presentation.multi_chain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.enums.SUPPORT_NETWORKS
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
            supportNetworks = SUPPORT_NETWORKS
        )
    )
    private val _uiEvent = Channel<ChainUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.data.collect {
                withContext(Dispatchers.Main) {
                    state = state.copy(selectedOne = it.network)
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
                state = state.copy(selectedOne = event.network)
            }
        }
    }

    private suspend fun updateChain() {
        appSettings.updateData {
            it.copy(network = state.selectedOne)
        }
    }
}
