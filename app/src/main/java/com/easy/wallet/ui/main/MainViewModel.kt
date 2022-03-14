package com.easy.wallet.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.assets.presentation.assets.MainUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val _uiEvent = Channel<MainUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiEvent(event: MainUIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}