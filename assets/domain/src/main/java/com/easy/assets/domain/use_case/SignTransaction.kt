package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.NetworkChain
import javax.inject.Inject

class SignTransaction @Inject constructor(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(chain: NetworkChain, plan: TransactionPlan): String {
        return assetRepository.signTransaction(chain, plan)
    }
}
