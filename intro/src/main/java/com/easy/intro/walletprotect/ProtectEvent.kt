package com.easy.intro.walletprotect

sealed class ProtectEvent {
    data class OnPasscodeUpdated(val passcode: String): ProtectEvent()
    data class OnBiometricChanged(val enabled: Boolean, val error: String): ProtectEvent()
    object OnCreated: ProtectEvent()
}