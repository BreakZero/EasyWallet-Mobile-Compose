package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository

class CoinAddress(
    private val assetRepository: AssetRepository
) {
    operator fun invoke(slug: String, isLegacy: Boolean): String {
        return assetRepository.address(slug, isLegacy)
    }
}
