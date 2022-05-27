package com.easy.assets.presentation.assets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.assets.presentation.routers.AssetRouter
import com.easy.core.common.Navigator
import com.easy.core.common.UiEvent
import com.easy.core.common.parameter
import com.easy.core.ui.GlobalRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletAssetViewModel @Inject constructor(
    private val assetsUseCases: AssetsUseCases
): ViewModel() {
    var assetState by mutableStateOf(AssetState(isLoading = true))
        private set
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val result = assetsUseCases.assetsWithBalance()
            assetState = assetState.copy(tokenLists = result, isLoading = false)
        }
    }

    fun onEvent(event: AssetEvent) {
        when (event) {
            is AssetEvent.ItemClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.NavigateTo(
                            Navigator(router = AssetRouter.ASSET_DETAIL) {
                                parameter {
                                    "slug" to event.assetInfo.slug
                                }
                            }
                        )
                    )
                }
            }
            is AssetEvent.ScanClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.NavigateTo(
                            Navigator(router = GlobalRouter.GLOBAL_SCAN)
                        )
                    )
                }
            }
            is AssetEvent.SettingsClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.NavigateTo(
                            Navigator(router = AssetRouter.SETTINGS)
                        )
                    )
                }
            }
            is AssetEvent.SwipeToRefresh -> {
                viewModelScope.launch {
                    assetState = assetState.copy(isLoading = true)
                    val result = assetsUseCases.assetsWithBalance()
                    assetState = assetState.copy(tokenLists = result, isLoading = false)
                }
            }
        }
    }
}