package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository

class AssetsWithBalance(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(): Result<List<AssetInfo>> {
        return assetRepository.assetsWithBalance()
    }
}