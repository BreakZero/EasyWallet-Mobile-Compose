package com.easy.wallet.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.GlobalHolder
import com.easy.intro.local.WalletDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wallet.core.jni.HDWallet
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val walletDatabase: WalletDatabase
) : ViewModel() {
    private val _uiEvent = Channel<Boolean>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            walletDatabase.walletDao.activeWallet()
                .collect {
                    delay(2000)
                    it?.also {
                        GlobalHolder.inject(HDWallet(it.mnemonic, it.passphrase))
                        _uiEvent.send(true)
                    } ?: kotlin.run {
                        _uiEvent.send(false)
                    }
                }
        }
    }
}
