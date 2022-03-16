package com.easy.assets.domain.use_case

import com.easy.assets.domain.model.Transaction
import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.ChainId
import javax.inject.Inject

class AssetTransactions @Inject constructor(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(
        address: String, chainId: ChainId, offset: Int, limit: Int, contractAddress: String?
    ): Result<List<Transaction>> {
        return repository.transactions(address, chainId, offset, limit, contractAddress)
    }
}