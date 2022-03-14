package com.easy.assets.presentation.send

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor() : ViewModel() {
    var sendFlowState by mutableStateOf(SendFlowState())

    fun onAddressChanged(address: String) {
        sendFlowState = sendFlowState.copy(address = address)
    }

    fun onAmountChanged(amount: String) {
        sendFlowState = sendFlowState.copy(amount = amount)
    }

    fun onGasChanged(gas: Long) {
        sendFlowState = sendFlowState.copy(gas = gas)
    }
}