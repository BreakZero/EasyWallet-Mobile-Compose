package com.easy.assets.presentation.send

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.errors.InsufficientBalanceException
import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.assets.domain.use_case.validation.ValidateAmount
import com.easy.core.common.UiEvent
import com.easy.core.common.UiText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
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
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(slug) as T
            }
        }
    }

    private val validateAmount: ValidateAmount = ValidateAmount()
    private var rawData: String? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var sendingState by mutableStateOf(SendingState())

    init {
        viewModelScope.launch {
            val currAsset = assetsUseCases.assets().find { it.slug == slug }
            sendingState = sendingState.copy(assetInfo = currAsset)
        }
    }

    fun onEvent(event: SendingFormEvent) {
        when (event) {
            is SendingFormEvent.AmountChanged -> {
                sendingState = sendingState.copy(amount = event.amount)
            }
            is SendingFormEvent.AddressChanged -> {
                sendingState = sendingState.copy(toAddress = event.address)
            }
            is SendingFormEvent.ActionChanged -> {
                sendingState = sendingState.copy(action = event.action)
            }
            is SendingFormEvent.Submit -> {
                signTransaction()
            }
            is SendingFormEvent.Broadcast -> {
                rawData?.also {
                    broadcastTransaction(it)
                }
            }
        }
    }

    private fun signTransaction() {
        val amountResult = validateAmount.execute(sendingState.amount)
        sendingState.assetInfo?.let {
            val addressResult =
                assetsUseCases.validateAddress.invoke(it.chain, sendingState.toAddress)
            val hasError = listOf(amountResult, addressResult).any {
                !it.successful
            }
            sendingState = sendingState.copy(
                amountError = amountResult.errorMessage,
                addressError = addressResult.errorMessage
            )
            if (hasError) {
                return
            }

            viewModelScope.launch {
                try {
                    rawData = assetsUseCases.signTransaction(
                        it.chain, TransactionPlan(
                            amount = sendingState.amount.toBigDecimal().movePointRight(it.decimal)
                                .toBigInteger(),
                            to = sendingState.toAddress,
                            contract = it.contractAddress
                        )
                    )
                    _uiEvent.send(UiEvent.Success)
                } catch (e: Exception) {
                    when (e) {
                        is InsufficientBalanceException -> {
                            _uiEvent.send(
                                UiEvent.ShowSnackbar(
                                    UiText.DynamicString(
                                        e.message ?: "insufficient balance"
                                    )
                                )
                            )
                        }
                        else -> {
                            _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("signing transaction with unknown error")))
                        }
                    }
                }
            }
        }
    }

    private fun broadcastTransaction(rawData: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendingState.assetInfo?.run {
                val result = assetsUseCases.broadcast.invoke(this.chain, rawData)
                if (result.isSuccess) {
                    Timber.tag("Easy").d("===${result.getOrNull().orEmpty()}")
                    _uiEvent.send(UiEvent.NavigateUp)
                } else {
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.DynamicString(
                                result.exceptionOrNull()?.message ?: "broadcast transaction failed"
                            )
                        )
                    )
                }
            }
        }
    }
}