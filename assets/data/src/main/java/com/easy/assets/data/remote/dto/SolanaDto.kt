package com.easy.assets.data.remote.dto

import com.google.gson.annotations.SerializedName


internal data class SolBalanceDto(
    @SerializedName("context")
    val context: SolContext,
    @SerializedName("value")
    val value: Long
)

internal data class SolContext(
    @SerializedName("slot")
    val slot: Long
)


internal data class RecentBlockHashResult(
    @SerializedName("context")
    val context: SolContext,
    @SerializedName("value")
    val value: RecentBlockHashValue
)

internal data class RecentBlockHashValue(
    @SerializedName("blockhash")
    val blockhash: String,
    @SerializedName("feeCalculator")
    val feeCalculator: FeeCalculator
)

internal data class FeeCalculator(
    @SerializedName("lamportsPerSignature")
    val lamportsPerSignature: Int
)

internal data class SolTransactionDto(
    val blockTime: Long,
    val slot: Long,
    val txHash: String,
    val fee: Long,
    val status: String,
    val lamport: Int
)

