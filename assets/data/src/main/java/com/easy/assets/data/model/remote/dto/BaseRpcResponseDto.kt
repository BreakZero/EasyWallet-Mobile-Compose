package com.easy.assets.data.model.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BaseRpcResponseDto<T>(
    @SerialName("id")
    val id: Int,
    @SerialName("jsonrpc")
    val jsonrpc: String,
    @SerialName("result")
    val result: T,
    @SerialName("error")
    val error: RpcError? = null
)

@Serializable
internal data class RpcError(
    val code: Int,
    val message: String
)
