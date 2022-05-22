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
    @SerializedName("blockTime")
    val blockTime: Int,
    @SerializedName("fee")
    val fee: Int,
    @SerializedName("lamport")
    val lamport: Int,
    @SerializedName("parsedInstruction")
    val parsedInstruction: List<ParsedInstruction>,
    @SerializedName("signer")
    val signer: List<String>,
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("txHash")
    val txHash: String
)

internal data class ParsedInstruction(
    @SerializedName("program")
    val program: String,
    @SerializedName("programId")
    val programId: String,
    @SerializedName("type")
    val type: String
)

