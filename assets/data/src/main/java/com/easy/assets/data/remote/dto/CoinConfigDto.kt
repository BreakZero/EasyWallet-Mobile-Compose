package com.easy.assets.data.remote.dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoinConfigDto(
    @SerializedName("coinSlug")
    val coinSlug: String,
    @SerializedName("coinSymbol")
    val coinSymbol: String,
    @SerializedName("contractAddress")
    val contractAddress: String?,
    @SerializedName("decimal")
    val decimal: Int,
    @SerializedName("iconUrl")
    val iconUrl: String,
    @SerializedName("tag")
    val tag: String?
)