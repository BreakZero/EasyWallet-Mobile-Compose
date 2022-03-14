package com.easy.assets.presentation.send.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SendAddressViewModel @Inject constructor() : ViewModel() {
    var address by mutableStateOf("")

    fun onAddressChanged(address: String) {
        this.address = address
    }
}