package com.easy.assets.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BaseRpcResponseDto<T>(
    @SerializedName("id")
    val id: Int,
    @SerializedName("jsonrpc")
    val jsonrpc: String,
    @SerializedName("result")
    val result: T
)
