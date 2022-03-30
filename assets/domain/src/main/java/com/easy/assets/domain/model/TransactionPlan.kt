package com.easy.assets.domain.model

import androidx.annotation.Keep
import java.math.BigInteger

@Keep
data class TransactionPlan(
    val amount: BigInteger,
    val from: String,
    val to: String,
    val memo: String? = null,
    val gasLimit: Long,
    val contract: String? = null
)
