package com.easy.assets.data.model.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SolBalanceDto(
    @SerialName("context")
    val context: SolContext,
    @SerialName("value")
    val value: Long
)

@Serializable
internal data class SolContext(
    @SerialName("slot")
    val slot: Long
)

@Serializable
internal data class RecentBlockHashResult(
    @SerialName("context")
    val context: SolContext,
    @SerialName("value")
    val value: RecentBlockHashValue
)

@Serializable
internal data class RecentBlockHashValue(
    @SerialName("blockhash")
    val blockhash: String,
    @SerialName("feeCalculator")
    val feeCalculator: FeeCalculator
)

@Serializable
internal data class FeeCalculator(
    @SerialName("lamportsPerSignature")
    val lamportsPerSignature: Int
)

@Serializable
internal data class SolTransactionDto(
    @SerialName("blockTime")
    val blockTime: Int,
    @SerialName("fee")
    val fee: Int,
    @SerialName("lamport")
    val lamport: Int,
    @SerialName("parsedInstruction")
    val parsedInstruction: List<ParsedInstruction>,
    @SerialName("signer")
    val signer: List<String>,
    @SerialName("slot")
    val slot: Int,
    @SerialName("status")
    val status: String,
    @SerialName("txHash")
    val txHash: String
)

@Serializable
internal data class ParsedInstruction(
    @SerialName("program")
    val program: String,
    @SerialName("programId")
    val programId: String,
    @SerialName("type")
    val type: String
)
