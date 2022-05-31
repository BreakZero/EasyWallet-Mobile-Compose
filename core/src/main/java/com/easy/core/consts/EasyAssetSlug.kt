package com.easy.core.consts

import com.google.gson.annotations.SerializedName

object EasyAssetSlug {
    const val SLUG_ETHEREUM = "ethereum"
    const val SLUG_BITCOIN = "bitcoin"
    const val SLUG_CARDANO = "cardano"
    const val SLUG_POLYGON = "polygon"
    const val SLUG_CRONOS = "cronos"
    const val SLUG_SOLANA = "solana"
}

enum class NetworkChain {
    @SerializedName("ethereum")
    Ethereum,
    @SerializedName("bitcoin")
    Bitcoin,
    @SerializedName("cardano")
    Cardano,
    @SerializedName("polygon")
    Polygon,
    @SerializedName("cronos")
    Cronos,
    @SerializedName("solana")
    Solana
}

enum class AssetTag {
    @SerializedName("ERC20")
    ERC20
}
