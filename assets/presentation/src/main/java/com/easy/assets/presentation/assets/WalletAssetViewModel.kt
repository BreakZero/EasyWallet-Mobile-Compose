package com.easy.assets.presentation.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.use_case.AssetsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletAssetViewModel @Inject constructor(
    private val assetsUseCases: AssetsUseCases
): ViewModel() {
    var assetState by mutableStateOf(AssetState(isLoading = true))
        private set
    private val _uiEvent = Channel<AssetUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(false)
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch {
            val result = assetsUseCases.assets()
            assetState = assetState.copy(tokenResult = result, isLoading = false)
        }
    }

    fun onEvent(event: AssetEvent) {
        when (event) {
            is AssetEvent.OnItemClick -> {
                viewModelScope.launch {
                    _uiEvent.send(AssetUIEvent.OnItemClick(event.assetInfo))
                }
            }
            is AssetEvent.OnReceive -> {
                viewModelScope.launch {
                    _uiEvent.send(AssetUIEvent.OnReceiveClick)
                }
            }
            is AssetEvent.OnSend -> {
                viewModelScope.launch {
                    _uiEvent.send(AssetUIEvent.OnSendClick)
                }
            }
            is AssetEvent.OnRefresh -> {
                viewModelScope.launch {
                    assetState = assetState.copy(
                        isLoading = true,
                        tokenResult = Result.success(mapOf())
                    )
                    val result = assetsUseCases.assets()
                    assetState = assetState.copy(tokenResult = result, isLoading = false)
                }
            }
        }
    }
}