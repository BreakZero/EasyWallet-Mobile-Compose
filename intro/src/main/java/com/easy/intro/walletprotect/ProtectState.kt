package com.easy.intro.walletprotect

data class ProtectState(
    val passcode: String = "",
    val biometric: Boolean = false
)