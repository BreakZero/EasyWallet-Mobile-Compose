package com.easy.intro.walletprotect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.common.UiEvent
import com.easy.core.common.UiText
import com.easy.wallets.data.WalletEntity
import com.easy.wallets.repository.WalletRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import wallet.core.jni.HDWallet
import javax.inject.Inject

@HiltViewModel
class WalletProtectViewModel @Inject constructor(
    private val walletRepository: WalletRepositoryImpl
): ViewModel() {
    var state by mutableStateOf(ProtectState())

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun initPasscode(passcode: String) {
        state = state.copy(passcode = passcode)
    }

    fun onEvent(event: ProtectEvent) {
        when (event) {
            is ProtectEvent.OnPasscodeUpdated -> {
                state = state.copy(passcode = event.passcode)
            }
            is ProtectEvent.OnBiometricChanged -> {
                if (event.error.isNotEmpty()) {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString(event.error)))
                    }
                } else {
                    state = state.copy(biometric = event.enabled)
                }
            }
            is ProtectEvent.OnCreated -> {
                viewModelScope.launch {
                    val result = HDWallet(128, "").also {
                        walletRepository.inject(it)
                    }.mnemonic()
                    walletRepository.insertWallet(
                        WalletEntity(mnemonic = result, 1, passphrase = "")
                    )
                    _uiEvent.send(UiEvent.Success)
                }
            }
            else -> Unit
        }
    }
}