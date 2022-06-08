package com.easy.core.consts

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class NetworkChain {
    @SerialName("ethereum")
    Ethereum,
    @SerialName("bitcoin")
    Bitcoin,
    @SerialName("cardano")
    Cardano,
    @SerialName("polygon")
    Polygon,
    @SerialName("cronos")
    Cronos,
    @SerialName("solana")
    Solana
}

@kotlinx.serialization.Serializable
enum class AssetTag {
    @SerialName("ERC20")
    ERC20
}
