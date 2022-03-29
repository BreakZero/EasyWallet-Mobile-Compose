package com.easy.intro.passcode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easy.core.common.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class PasscodeViewModel @Inject constructor(): ViewModel() {
    var passcodeState by mutableStateOf(PasscodeState(messageLabel = "Enter passcode"))
    private val _uiEvent = Channel<Result<String>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: PasscodeEvent) {
        when (event) {
            is PasscodeEvent.Delete -> {
                passcodeState = passcodeState.delete()
            }
            is PasscodeEvent.Insert -> {
                passcodeState = passcodeState.insert(event.number)
                if (passcodeState.passcode.length == 6) {
                    if (passcodeState.originPasscode.isNotEmpty()) {
                        if (passcodeState.originPasscode == passcodeState.passcode) {
                            viewModelScope.launch {
                                _uiEvent.send(Result.success(passcodeState.passcode))
                            }
                        } else {
                            passcodeState = passcodeState.clear("Enter passcode", "not match")
                        }
                    } else {
                        passcodeState = passcodeState.originDone("Enter new passcode")
                    }
                }
            }
            else -> Unit
        }
    }
}