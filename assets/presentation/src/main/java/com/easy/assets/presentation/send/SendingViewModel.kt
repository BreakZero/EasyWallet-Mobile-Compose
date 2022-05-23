package com.easy.assets.presentation.send

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.core.common.UiEvent
import com.easy.core.common.UiText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SendingViewModel @AssistedInject constructor(
    private val assetsUseCases: AssetsUseCases,
    @Assisted private val slug: String
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(slug: String): SendingViewModel
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var sendingState by mutableStateOf(SendingState())

    init {
        viewModelScope.launch {
            val currAsset = assetsUseCases.assets().find { it.slug == slug }
            sendingState = sendingState.copy(assetInfo = currAsset)
        }
    }

    fun onAmountChanged(amount: String) {
        sendingState = sendingState.copy(amount = amount)
    }

    fun onToAddressChanged(address: String) {
        sendingState = sendingState.copy(toAddress = address)
    }

    fun onActionChanged(action: Action) {
        sendingState = sendingState.copy(action = action)
    }

    fun onSign() {
        viewModelScope.launch {
            assetsUseCases.assets().find { it.slug == slug }?.run {
                val rawData = assetsUseCases.signTransaction(
                    slug, TransactionPlan(
                        amount = sendingState.amount.toBigDecimal().movePointRight(this.decimal)
                            .toBigInteger(),
                        to = sendingState.toAddress,
                        gasLimit = 21000L,
                        contract = null
                    )
                )
                Timber.tag("easy").d("==== $rawData")
                _uiEvent.send(UiEvent.Success)
            } ?: kotlin.run {
                _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("somethings went wrong")))
            }
        }
    }
}