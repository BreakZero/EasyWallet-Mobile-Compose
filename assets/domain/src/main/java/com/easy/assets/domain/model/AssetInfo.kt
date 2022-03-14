package com.easy.assets.domain.model

import androidx.annotation.Keep

@Keep
data class AssetInfo(
    val slug: String,
    val symbol: String,
    val decimal: Int,
    val icon: String,
    val contractAddress: String?,
    val tag: String? = null
)
