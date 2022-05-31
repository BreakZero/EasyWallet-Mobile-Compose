package com.easy.assets.domain.model

import androidx.annotation.Keep
import com.easy.core.consts.AssetChain
import com.easy.core.consts.AssetTag

@Keep
data class AssetInfo(
    val slug: String,
    val chain: AssetChain,
    val symbol: String,
    val decimal: Int,
    val icon: String,
    val contractAddress: String?,
    val tag: AssetTag? = null,
    val balance: String
)
