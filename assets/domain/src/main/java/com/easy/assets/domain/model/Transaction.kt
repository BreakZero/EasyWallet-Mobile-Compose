package com.easy.assets.domain.model

import androidx.annotation.Keep

@Keep
data class Transaction(
    val txHash: String,
    val value: String,
    val from: String,
    val to: String,
    val timeStamp: String,
    val inputData: String
)
