package com.easy.assets.data.model.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class EthTxDto(
    @SerializedName("blockHash")
    val blockHash: String,
    @SerializedName("blockNumber")
    val blockNumber: String,
    @SerializedName("confirmations")
    val confirmations: String,
    @SerializedName("contractAddress")
    val contractAddress: String,
    @SerializedName("cumulativeGasUsed")
    val cumulativeGasUsed: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("gas")
    val gas: String,
    @SerializedName("gasPrice")
    val gasPrice: String,
    @SerializedName("gasUsed")
    val gasUsed: String,
    @SerializedName("hash")
    val hash: String,
    @SerializedName("input")
    val input: String,
    @SerializedName("isError")
    val isError: String,
    @SerializedName("nonce")
    val nonce: String,
    @SerializedName("timeStamp")
    val timeStamp: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("transactionIndex")
    val transactionIndex: String,
    @SerializedName("txreceipt_status")
    val txreceiptStatus: String,
    @SerializedName("value")
    val value: String
)
