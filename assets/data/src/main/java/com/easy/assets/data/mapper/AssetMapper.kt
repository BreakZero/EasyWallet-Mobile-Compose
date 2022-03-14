package com.easy.assets.data.mapper

import com.easy.assets.data.remote.dto.CoinConfigDto
import com.easy.assets.domain.model.AssetInfo

fun CoinConfigDto.toAsset(): AssetInfo {
    return AssetInfo(
        slug = this.coinSlug,
        symbol = this.coinSymbol,
        decimal = this.decimal,
        contractAddress = this.contractAddress,
        icon = this.iconUrl,
        tag = this.tag,
    )
}