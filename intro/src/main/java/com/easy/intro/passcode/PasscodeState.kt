package com.easy.intro.passcode

import androidx.annotation.Keep

@Keep
data class PasscodeState(
    val passcode: String = "",
    val originPasscode: String = "",
    val messageLabel: String,
    val error: String? = null
) {
    fun insert(number: String): PasscodeState {
        return PasscodeState(passcode = "$passcode$number", messageLabel = messageLabel, originPasscode = originPasscode)
    }
    fun delete(): PasscodeState {
        return PasscodeState(passcode = passcode.dropLast(1), messageLabel = messageLabel, originPasscode = originPasscode)
    }
    fun clear(messageLabel: String, error: String): PasscodeState {
        return PasscodeState(passcode = "", originPasscode = "", messageLabel = messageLabel, error = error)
    }
    fun originDone(messageLabel: String): PasscodeState {
        val originPasscode = passcode
        return PasscodeState(passcode = "", originPasscode = originPasscode, messageLabel = messageLabel, error = null)
    }
}
