package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.NetworkChain
import javax.inject.Inject

class BroadcastTransaction @Inject constructor(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(chain: NetworkChain, rawData: String): Result<String> {
        return assetRepository.broadcastTransaction(chain, rawData)
    }
}
