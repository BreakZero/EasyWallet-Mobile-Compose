package com.easy.assets.data.model.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class EthTxResponseDto(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: List<EthTxDto>
)
