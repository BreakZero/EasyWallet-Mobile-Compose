package com.easy.assets.data.mapper

import com.easy.assets.data.model.remote.dto.CoinConfigDto
import com.easy.assets.domain.model.AssetInfo

internal fun CoinConfigDto.toAsset(
    balance: String = "0.0"
): AssetInfo {
    return AssetInfo(
        slug = this.coinSlug,
        symbol = this.coinSymbol,
        decimal = this.decimal,
        contractAddress = this.contractAddress,
        icon = this.iconUrl,
        tag = this.tag,
        balance = balance
    )
}