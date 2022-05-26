package com.easy.assets.domain.model

import androidx.annotation.Keep
import java.math.BigInteger

@Keep
data class TransactionPlan(
    val amount: BigInteger,
    val to: String,
    val memo: String? = null,
    val contract: String? = null
)
