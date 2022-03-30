package com.easy.assets.data.remote.dto

import com.google.gson.annotations.SerializedName

internal data class FeeHistoryDto(
    @SerializedName("baseFeePerGas")
    val baseFeePerGas: List<String>,
    @SerializedName("gasUsedRatio")
    val gasUsedRatio: List<Double>,
    @SerializedName("oldestBlock")
    val oldestBlock: String,
    @SerializedName("reward")
    val reward: List<List<String>>
)