package com.easy.settings.presentation.multi_chain

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.enums.Chain
import com.easy.core.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            appSettings.data.collect {
                state = state.copy(checkId = it.chain.id)
            }
        }
    }

    fun onChainChanged(chain: Chain) {
        state = state.copy(checkId = chain.id)
    }

    fun setChain() {
        viewModelScope.launch {
            appSettings.updateData {
                it.copy(chain = state.selected().also {
                    Log.d("=====", it.toString())
                })
            }
        }
    }
}
