package com.easy.assets.data.model.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EthTxResponseDto(
    @SerialName("status")
    val status: String,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: List<EthTxDto>
)
