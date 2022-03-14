package com.easy.assets.presentation.send

import androidx.annotation.Keep

@Keep
data class SendFlowState(
    val address: String = "",
    val amount: String = "",
    val memo: String? = null,
    val gas: Long = 0L,
    val gasLimit: Long = 0L
)