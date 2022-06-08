package com.easy.assets.data.model.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EthTxDto(
    @SerialName("blockHash")
    val blockHash: String,
    @SerialName("blockNumber")
    val blockNumber: String,
    @SerialName("confirmations")
    val confirmations: String,
    @SerialName("contractAddress")
    val contractAddress: String,
    @SerialName("cumulativeGasUsed")
    val cumulativeGasUsed: String,
    @SerialName("from")
    val from: String,
    @SerialName("gas")
    val gas: String,
    @SerialName("gasPrice")
    val gasPrice: String,
    @SerialName("gasUsed")
    val gasUsed: String,
    @SerialName("hash")
    val hash: String,
    @SerialName("input")
    val input: String,
    @SerialName("isError")
    val isError: String? = null,
    @SerialName("nonce")
    val nonce: String,
    @SerialName("timeStamp")
    val timeStamp: String,
    @SerialName("to")
    val to: String,
    @SerialName("transactionIndex")
    val transactionIndex: String,
    @SerialName("txreceipt_status")
    val txreceiptStatus: String? = null,
    @SerialName("value")
    val value: String
)
