package com.easy.dapp.presentation.detail

import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.use_case.AssetsUseCases
import com.easy.core.consts.NetworkChain
import com.easy.dapp.presentation.common.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class DAppDetailViewModel @Inject constructor(
    private val assetsUseCases: AssetsUseCases
) : ViewModel() {
    var dAppDetailState by mutableStateOf(DAppDetailState())

    // leak field
    var webView: WebView? = null
        private set

    fun injectWebView(webView: WebView) {
        this.webView = webView
    }

    init {
        viewModelScope.launch {
            val balance = assetsUseCases.balance.invoke(NetworkChain.Ethereum, null)
            Timber.tag("Easy").d("===== balance: $balance")
        }
    }

    fun onReceiveMethod(messageInfo: MessageInfo) {
        dAppDetailState = dAppDetailState.copy(showDialog = true, messageInfo = messageInfo)
    }

    fun approve() {
        dAppDetailState = dAppDetailState.copy(showDialog = false)
    }

    fun reject() {
        dAppDetailState = dAppDetailState.copy(showDialog = false)
    }

    private suspend fun signTransaction(to: String, data: String) {
        assetsUseCases.signTransaction(
            NetworkChain.Ethereum, TransactionPlan(
                amount = BigInteger.ZERO,
                to = to,
                payload = data
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        webView?.destroy()
        this.webView = null
    }
}