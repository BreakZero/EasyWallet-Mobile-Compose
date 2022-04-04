package com.easy.intro.walletimport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.GlobalHolder
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
class WalletImportViewModel @Inject constructor(
    private val walletRepository: WalletRepositoryImpl
): ViewModel() {
    var state by mutableStateOf(ImportState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ImportEvent) {
        when (event) {
            ImportEvent.OnImportClick -> {
                kotlin.runCatching {
                    HDWallet(state.phrase, "")
                }.onFailure {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.ShowSnackbar(UiText.DynamicString("invalid mnemonic, please try another")))
                    }
                }.onSuccess {
                    GlobalHolder.inject(it)
                    viewModelScope.launch {
                        walletRepository.insertWallet(
                            WalletEntity(
                                mnemonic = state.phrase, 1, passphrase = ""
                            )
                        )
                        _uiEvent.send(UiEvent.Success)
                    }
                }
            }
            is ImportEvent.OnFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.phrase.isBlank()
                )
            }
            is ImportEvent.OnPhraseChange -> {
                state = state.copy(phrase = event.phrase)
            }
        }
    }

    fun onNavigateUp() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}