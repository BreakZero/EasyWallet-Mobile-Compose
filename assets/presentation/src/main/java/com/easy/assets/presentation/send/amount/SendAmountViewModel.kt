package com.easy.assets.presentation.send.amount

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendAmountViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(SendAmountState())

    fun onAmountChanged(amount: String) {
        this.state = state.copy(amount = amount)
    }

    fun onMemoChanged(memo: String) {
        this.state = state.copy(memo = memo)
    }
}