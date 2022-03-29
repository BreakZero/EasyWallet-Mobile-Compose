package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.TransactionPlan
import com.easy.assets.domain.repository.AssetRepository
import javax.inject.Inject

class SignTransaction @Inject constructor(
    private val assetRepository: AssetRepository
) {
    suspend operator fun invoke(slug: String, plan: TransactionPlan) {
        assetRepository.signTransaction(slug, plan)
    }
}