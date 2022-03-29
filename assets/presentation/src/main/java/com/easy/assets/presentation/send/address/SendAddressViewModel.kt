package com.easy.assets.presentation.send.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.assets.presentation.send.SendFlowState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import logcat.logcat

class SendAddressViewModel @AssistedInject constructor(
    private val assetsUseCases: AssetsUseCases,
    @Assisted private val slug: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(slug: String): SendAddressViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            slug: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(slug) as T
            }
        }
    }

    var sendFlowState by mutableStateOf(SendFlowState())

    init {
        viewModelScope.launch {
            val currAsset = assetsUseCases.assets().find { it.slug == slug }
            sendFlowState = sendFlowState.copy(assetInfo = currAsset)
        }
    }

    fun onAddressChanged(address: String) {
        sendFlowState = sendFlowState.copy(toAddress = address)
    }
}