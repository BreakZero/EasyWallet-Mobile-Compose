package com.easy.assets.data.model.remote.dto


import androidx.annotation.Keep
import com.easy.core.consts.NetworkChain
import com.easy.core.consts.AssetTag
import com.google.gson.annotations.SerializedName

@Keep
internal data class CoinConfigDto(
    @SerializedName("chain")
    val chain: NetworkChain,
    @SerializedName("coinSymbol")
    val coinSymbol: String,
    @SerializedName("contractAddress")
    val contractAddress: String?,
    @SerializedName("decimal")
    val decimal: Int,
    @SerializedName("iconUrl")
    val iconUrl: String,
    @SerializedName("tag")
    val tag: AssetTag?
) {
    fun slug(): String {
        return "${chain.name}-$coinSymbol-${tag?.name}"
    }
}