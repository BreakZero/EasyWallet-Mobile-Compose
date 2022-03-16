package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository

class Assets(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(): List<AssetInfo> {
        return assetRepository.assets()
    }
}