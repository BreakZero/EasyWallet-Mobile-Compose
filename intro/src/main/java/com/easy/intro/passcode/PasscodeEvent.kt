package com.easy.intro.passcode

sealed class PasscodeEvent {
    data class Insert(val number: String) : PasscodeEvent()
    object Done : PasscodeEvent()
    object Delete : PasscodeEvent()
}
