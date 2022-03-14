package com.easy.intro.walletcreate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletCreateViewModel @Inject constructor(): ViewModel() {
    var state by mutableStateOf(false)

    fun toggle(boolean: Boolean) {
        state = boolean
    }
}