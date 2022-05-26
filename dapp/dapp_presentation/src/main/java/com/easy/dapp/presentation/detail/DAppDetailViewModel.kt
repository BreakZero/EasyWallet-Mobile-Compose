package com.easy.dapp.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.easy.dapp.presentation.common.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DAppDetailViewModel @Inject constructor() : ViewModel() {
    var dAppDetailState by mutableStateOf(DAppDetailState())

    fun onReceiveMethod(messageInfo: MessageInfo) {
        dAppDetailState = dAppDetailState.copy(showDialog = true, messageInfo = messageInfo)
    }

    fun approve() {
        dAppDetailState = dAppDetailState.copy(showDialog = false)
    }

    fun reject() {
        dAppDetailState = dAppDetailState.copy(showDialog = false)
    }
}