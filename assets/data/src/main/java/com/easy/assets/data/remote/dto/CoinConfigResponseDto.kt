package com.easy.assets.data.remote.dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoinConfigResponseDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val result: List<CoinConfigDto>,
    @SerializedName("error")
    val error: String?
)