package com.easy.assets.domain.use_case

import com.easy.assets.domain.repository.AssetRepository
import com.easy.core.consts.ChainId
import java.math.BigInteger

class AssetBalance(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(
        address: String, chainId: ChainId, contractAddress: String
    ): BigInteger {
        return repository.balance(address, chainId, contractAddress)
    }
}
