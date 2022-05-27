package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import javax.inject.Inject

class BroadcastTransaction @Inject constructor(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(slug: String, rawData: String): Result<String> {
        return assetRepository.broadcastTransaction(slug, rawData)
    }
}