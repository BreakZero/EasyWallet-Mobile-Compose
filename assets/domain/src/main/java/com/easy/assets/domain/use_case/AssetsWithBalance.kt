package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository
import javax.inject.Inject

class AssetsWithBalance @Inject constructor(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(): Result<List<AssetInfo>> {
        return assetRepository.assetsWithBalance()
    }
}