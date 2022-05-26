package com.easy.assets.data.model.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class BaseRpcResponseDto<T>(
    @SerializedName("id")
    val id: Int,
    @SerializedName("jsonrpc")
    val jsonrpc: String,
    @SerializedName("result")
    val result: T,
    @SerializedName("error")
    val error: RpcError?
)

@Keep
internal data class RpcError(
    val code: Int,
    val message: String
)
