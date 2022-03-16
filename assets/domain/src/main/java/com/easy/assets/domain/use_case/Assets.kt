package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.AssetInfo
import com.easy.assets.domain.repository.AssetRepository
import com.easy.assets.domain.repository.CoinRepository
import javax.inject.Inject

class Assets @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(): List<AssetInfo> {
        return coinRepository.assets()
    }
}