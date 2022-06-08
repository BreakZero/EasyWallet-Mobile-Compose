package com.easy.assets.data.model.remote.dto

import androidx.annotation.Keep
import com.easy.core.consts.AssetTag
import com.easy.core.consts.NetworkChain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoinConfigDto(
    @SerialName("chain")
    val chain: NetworkChain,
    @SerialName("coinSymbol")
    val coinSymbol: String,
    @SerialName("contractAddress")
    val contractAddress: String?,
    @SerialName("decimal")
    val decimal: Int,
    @SerialName("iconUrl")
    val iconUrl: String,
    @SerialName("tag")
    val tag: AssetTag? = null
) {
    fun slug(): String {
        return "${chain.name}-$coinSymbol-${tag?.name}"
    }
}
