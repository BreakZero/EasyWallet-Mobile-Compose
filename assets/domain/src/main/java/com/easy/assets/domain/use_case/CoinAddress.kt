package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import javax.inject.Inject

class CoinAddress @Inject constructor(
    private val assetRepository: AssetRepository
) {
    operator fun invoke(slug: String, isLegacy: Boolean = false): String {
        return assetRepository.address(slug, isLegacy)
    }
}
