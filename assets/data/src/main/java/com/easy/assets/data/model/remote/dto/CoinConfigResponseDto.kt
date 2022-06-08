package com.easy.assets.data.model.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoinConfigResponseDto(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val result: List<CoinConfigDto>,
    @SerialName("error")
    val error: String? = null
)
