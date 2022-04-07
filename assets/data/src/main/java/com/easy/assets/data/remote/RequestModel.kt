package com.easy.assets.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BaseRpcRequest(
    val jsonrpc: String,
    val method: String,
    @SerializedName("params")
    val params: List<Any>,
    val id: Int
)

@Keep
internal data class CallBalance(
    val data: String,
    val from: String,
    val to: String
)
