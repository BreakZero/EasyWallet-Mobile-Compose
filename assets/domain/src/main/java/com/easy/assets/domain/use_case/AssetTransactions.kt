package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.NetworkChain
import javax.inject.Inject

class AssetTransactions @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(
        chain: NetworkChain,
        offset: Int,
        limit: Int,
        contract: String?
    ): Result<List<Transaction>> {
        return repository.transactions(chain, offset, limit, contract)
    }
}
