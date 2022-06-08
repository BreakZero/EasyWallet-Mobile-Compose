package com.easy.assets.data.model.remote

import com.easy.assets.data.serializers.RpcRequestBodySerializer
import kotlinx.serialization.Serializable

@Serializable(with = RpcRequestBodySerializer::class)
internal data class BaseRpcRequest(
    val jsonrpc: String,
    val method: String,
    val params: List<Parameter>,
    val id: Int
)

internal interface Parameter

@Serializable
internal data class StringParameter(
    val content: String
): Parameter

@Serializable
internal data class CallParameter(
    val data: String,
    val from: String,
    val to: String
): Parameter

@Serializable
internal data class IntListParameter(
    val items: List<Int>
): Parameter
