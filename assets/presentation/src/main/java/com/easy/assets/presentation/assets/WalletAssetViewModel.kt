package com.easy.assets.presentation.assets

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.core.common.SecurityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletAssetViewModel @Inject constructor(
    private val assetsUseCases: AssetsUseCases,
    private val dataStore: DataStore<Preferences>,
    private val securityUtil: SecurityUtil
): ViewModel() {
    var assetState by mutableStateOf(AssetState(isLoading = true))
        private set
    private val _uiEvent = Channel<AssetUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(false)
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch {
            val t = securityUtil.encryptData("A", "Hello World")
            Log.d("=====", t.decodeToString())
            val d = securityUtil.decryptData("A", t)
            Log.d("=====", d)
            dataStore.data.map {
                val passcodeByte = it[stringPreferencesKey("PASS_CODE")] ?: "undefind"
                Log.d("=====", "$passcodeByte")
                val passcode = securityUtil.decryptData("PASS_CODE_ALIAS", passcodeByte.toByteArray())
                Log.d("=====", "$passcode")
            }
            val result = assetsUseCases.assetsWithBalance()
            assetState = assetState.copy(tokenLists = result, isLoading = false)
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
                    assetState = assetState.copy(isLoading = true)
                    val result = assetsUseCases.assetsWithBalance()
                    assetState = assetState.copy(tokenLists = result, isLoading = false)
                }
            }
        }
    }
}